package com.highfive.meetu.domain.resume.personal.service;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.coverletter.common.repository.CoverLetterRepository;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import com.highfive.meetu.domain.resume.common.repository.ResumeContentRepository;
import com.highfive.meetu.domain.resume.common.repository.ResumeRepository;
import com.highfive.meetu.domain.resume.personal.dto.ResumeContentPersonalDTO;
import com.highfive.meetu.domain.resume.personal.dto.ResumePersonalDTO;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumePersonalService {

    private final ResumeRepository resumeRepository;
    private final ProfileRepository profileRepository;
    private final CoverLetterRepository coverLetterRepository;
    private final ResumeContentRepository resumeContentRepository;

    /**
     * 주어진 프로필 ID에 해당하는 이력서 목록을 상태(status = 0) 기준으로 최신순 정렬하여 조회합니다.
     * 상태 0은 '활성 상태'의 이력서만 의미합니다.
     *
     * @param profileId 프로필 ID (개인 회원 기준)
     * @return 이력서 목록 DTO 리스트
     */
    public List<ResumePersonalDTO> getResumeListByProfileId(Long profileId) {

        // 상태가 '활성(0)'인 이력서들을 profileId 기준으로 최신순(updatedAt 기준 내림차순)으로 조회
        List<Resume> resumeList = resumeRepository.findAllByProfileIdAndStatusOrderByUpdatedAtDesc(profileId, 0);

        // 조회된 Resume 엔티티 리스트를 DTO 리스트로 변환하여 반환
        return resumeList.stream()
                .map(ResumePersonalDTO::fromEntity) // 간단 목록용 fromEntity
                .toList();
    }


    /**
     * 이력서 상세 조회 서비스
     * - Resume + ResumeContent 리스트를 JOIN FETCH로 한 번에 가져옴
     * - 가져온 Resume 엔티티를 DTO로 변환하여 반환
     */
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정 (쓰기 불필요, 성능 최적화)
    public ResumePersonalDTO getResumeDetail(Long resumeId) {

        // ====================== 1. 이력서 조회 ======================
        // 이력서 + 연관된 ResumeContent 항목들까지 함께 조회 (Fetch Join 방식)
        Resume resume = resumeRepository.findWithContentsById(resumeId)
                .orElseThrow(() -> new NotFoundException("이력서를 찾을 수 없습니다."));

        // ====================== 2. 이력서 항목 리스트 변환 ======================
        // 이력서에 포함된 항목(ResumeContent)들을 각각 ResumeContentPersonalDTO로 변환
        // resume.getResumeContentList()의 각 ResumeContent 객체를
        // ResumeContentPersonalDTO.fromEntity(content)로 변환하는 것과 동일
        // 메서드 레퍼런스(::)는 람다식 content -> ResumeContentPersonalDTO.fromEntity(content) 를 간결하게 표현한 문법
        List<ResumeContentPersonalDTO> contentDTOs = resume.getResumeContentList().stream()
                .map(ResumeContentPersonalDTO::fromEntity)
                .toList();

        // ====================== 3. 이력서 → DTO 변환 및 반환 ======================
        return ResumePersonalDTO.fromEntity(resume, contentDTOs); // 여기서 사용됨!
    }


    /**
     * 이력서 생성 서비스
     * - 이력서만 먼저 생성하고 ID 반환 (임시저장용 또는 초안)
     *
     * @param dto 작성 요청 DTO
     * @return 생성된 이력서 ID
     */
    @Transactional
    public Long createResume(ResumePersonalDTO dto) {

        // 1. 필수 값 검증
        if (dto.getTitle() == null || dto.getProfileId() == null) {
            throw new BadRequestException("이력서 제목 또는 프로필 ID는 필수입니다.");
        }

        // 2. 연관 프로필 조회
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

        // 3. 자기소개서가 선택된 경우 조회
        CoverLetter coverLetter = null;
        if (dto.getCoverLetterId() != null) {
            coverLetter = coverLetterRepository.findById(dto.getCoverLetterId())
                    .orElseThrow(() -> new NotFoundException("자기소개서를 찾을 수 없습니다."));
        }

        // 4. 이력서 엔티티 생성
        Resume resume = dto.toEntity(profile, coverLetter);

        // 5. 저장 (ID 생성됨)
        resumeRepository.save(resume);

        // 6. 항목이 있는 경우 content 리스트도 저장
        if (dto.getContents() != null && !dto.getContents().isEmpty()) {

            List<ResumeContent> contentList = dto.getContents().stream()
                    .map(content -> content.toEntity(resume))
                    .collect(Collectors.toList());

            resumeContentRepository.saveAll(contentList);
        }

        // 7. 생성된 ID만 반환
        return resume.getId();
    }


    /**
     * 이력서 전체 수정 서비스
     * - 이력서 기본 정보 + 항목 리스트 전체 갱신
     * - 기존 항목들은 모두 삭제 후 새로 저장
     */
    @Transactional
    public void updateResumeAll(Long resumeId, ResumePersonalDTO dto) {

        // 1. 수정 대상 이력서 조회
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NotFoundException("이력서를 찾을 수 없습니다."));

        // 2. 이력서 기본 정보 수정 (setter 대신 update 메서드 분리 가능)
        resume.setTitle(dto.getTitle());
        resume.setOverview(dto.getOverview());
        resume.setResumeType(dto.getResumeType());
        resume.setExtraLink1(dto.getExtraLink1());
        resume.setExtraLink2(dto.getExtraLink2());
        resume.setStatus(dto.getStatus());

        // 3. 기존 항목 전체 삭제
        resumeContentRepository.deleteAllByResumeId(resumeId);

        // 4. 새 항목 리스트 저장
        if (dto.getContents() != null && !dto.getContents().isEmpty()) { // 항목 리스트가 null이 아니고 비어 있지 않은 경우에만 처리
            List<ResumeContent> contentList = dto.getContents().stream()
                    .map(content -> content.toEntity(resume))
                    .collect(Collectors.toList());

            resumeContentRepository.saveAll(contentList); // 항목 리스트를 DB에 일괄 저장
        }
    }
}
