package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final LocationRepository locationRepository;

    /**
     * 통합 검색 (필터 + 키워드 + 정렬)
     */
    @Transactional(readOnly = true)
    public Page<JobPostingDTO> searchJobPostings(
            List<String> industry,
            Integer experienceLevel,
            Integer educationLevel,
            List<String> locationCode,
            String keyword,
            String sort,
            Pageable pageable
    ) {
        Page<JobPosting> jobPostings = jobPostingRepository.searchByFilters(
                industry, experienceLevel, educationLevel, locationCode, keyword, sort, pageable
        );

        return jobPostings.map(JobPostingDTO::fromEntity);
    }



    /**
     * 전체 목록(기본: 최신순) 조회 (페이지네이션 적용)
     */
    @Transactional(readOnly = true)
    public Page<JobPostingDTO> getJobPostingList(Pageable pageable) {
        return searchJobPostings(
                null, null, null, null,
                null, "newest",
                pageable
        );
    }

    /**
     * 단일 상세 조회
     */
    @Transactional(readOnly = true)
    public JobPostingDTO getJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채용공고입니다."));
        return JobPostingDTO.fromEntity(jobPosting);
    }

    /**
     * 시/도 선택 시 시군구까지 확장
     */
    private List<String> expandLocationCodes(List<String> inputCodes) {
        if (inputCodes == null || inputCodes.isEmpty()) return null;

        List<String> expanded = new ArrayList<>();

        for (String code : inputCodes) {
            locationRepository.findByLocationCode(code).ifPresent(provinceLoc -> {
                expanded.add(code); // 본인도 포함

                // 해당 시/도에 속한 시/군/구 추가
                List<String> childCodes = locationRepository.findByProvince(provinceLoc.getProvince())
                        .stream()
                        .map(loc -> loc.getLocationCode())
                        .filter(c -> !c.equals(code)) // 본인은 중복되므로 제거
                        .toList();

                expanded.addAll(childCodes);
            });
        }

        return expanded;
    }
}
