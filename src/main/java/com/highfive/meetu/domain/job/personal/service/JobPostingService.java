package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final LocationRepository locationRepository;

    /**
     * 통합 검색(필터+키워드+정렬)
     *
     * @param industry        산업(직무) 필터 (예: "개발", "마케팅"…)
     * @param experienceLevel 경력 필터 (예: 0=신입, 1=경력)
     * @param educationLevel  학력 필터 (예: 0=학력무관, 2=전문대졸…)
     * @param locationCode    지역코드 필터
     * @param keyword         키워드 포함 검색 (job.keyword 컬럼)
     * @param sort            정렬 기준 ("newest"|"popular"|"recommended")
     */
    @Transactional(readOnly = true)
    public List<JobPostingDTO> searchJobPostings(
            List<String>industry,
            Integer experienceLevel,
            Integer educationLevel,
            List<String> locationCode,
            String keyword,
            String sort
    ) {
        List<String> expandedLocationCodes = expandLocationCodes(locationCode);

        List<JobPosting> postings = jobPostingRepository.searchByFilters(
                industry,
                experienceLevel,
                educationLevel,
                expandedLocationCodes,
                keyword,
                sort
        );
        return postings.stream()
                .map(JobPostingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private List<String> expandLocationCodes(List<String> inputCodes) {
        if (inputCodes == null || inputCodes.isEmpty()) return null;

        List<String> expanded = new ArrayList<>();

        for (String code : inputCodes) {
            // 시/도만 선택된 경우 (예: "101000")
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

    /**
     * 전체 목록(기본: 최신순) 조회
     */
    @Transactional(readOnly = true)
    public List<JobPostingDTO> getJobPostingList() {
        // 모두 null + sort=newest 로 호출
        return searchJobPostings(
                null, null, null, null,
                null, "newest"
        );
    }

    /**
     * 단일 상세 조회
     */
    @Transactional(readOnly = true)
    public JobPostingDTO getJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("채용공고를 찾을 수 없습니다. id: " + jobPostingId));
        return JobPostingDTO.fromEntity(jobPosting);
    }
}
