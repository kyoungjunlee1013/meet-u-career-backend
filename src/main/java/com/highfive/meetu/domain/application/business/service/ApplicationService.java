package com.highfive.meetu.domain.application.business.service;

import com.highfive.meetu.domain.application.business.dto.ApplicationDetailDTO;
import com.highfive.meetu.domain.application.business.dto.ResumeApplicationDetailDTO;
import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetterContent;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import com.highfive.meetu.domain.resume.common.repository.ResumeContentRepository;
import com.highfive.meetu.domain.resume.common.repository.ResumeRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ResumeContentRepository resumeContentRepository;
    private final S3Service s3Service;

    public List<ApplicationDetailDTO> getApplications() {
        return applicationRepository.findApplicationDetails();
    }

    public ResumeApplicationDetailDTO getResumeDetailByApplicationId(Long applicationId) {
        // 1. 지원서 조회
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));

        Resume resume = application.getResume();

        // 2. 이력서 항목 정렬 조회
        List<ResumeContent> sortedResumeContents = resumeContentRepository.findByResumeIdOrderByContentOrderAsc(resume.getId());
        resume.setResumeContentList(sortedResumeContents);

        // 3. 자기소개서 항목 정렬 처리
        CoverLetter coverLetter = resume.getCoverLetter();
        if (coverLetter != null && coverLetter.getCoverLetterContentList() != null) {
            List<CoverLetterContent> sortedCoverLetterContents = coverLetter.getCoverLetterContentList().stream()
                .sorted(Comparator.comparingInt(CoverLetterContent::getContentOrder))
                .collect(Collectors.toList());
            coverLetter.setCoverLetterContentList(sortedCoverLetterContents);
        }

        // 4. S3 프리사인 URL 생성 (resumeFileKey가 존재할 때만)
        String presignedUrl = resume.getResumeFileKey() != null
            ? s3Service.generatePresignedUrl(resume.getResumeFileKey())
            : null;

        // 5. DTO로 변환
        return ResumeApplicationDetailDTO.from(application, presignedUrl);
    }
}
