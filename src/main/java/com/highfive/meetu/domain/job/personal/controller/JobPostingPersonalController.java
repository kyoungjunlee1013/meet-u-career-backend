package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDetailDTO;
import com.highfive.meetu.domain.job.personal.dto.LocationDTO;
import com.highfive.meetu.domain.job.personal.service.JobPostingPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal/job")
@RequiredArgsConstructor
public class JobPostingPersonalController {

    private final JobPostingPersonalService jobPostingService;
    private final LocationRepository locationRepository;

    /**
     * 전체/필터/검색/정렬 통합 엔드포인트
     *
     * - industry           : 산업(직무)
     * - experienceLevel    : 경력 레벨
     * - educationLevel     : 학력 레벨
     * - locationCode       : 지역 코드
     * - keyword            : 키워드 포함 검색
     * - sort               : newest|popular|recommended
     * - pageable           : 페이징 정보
     */
    @GetMapping("/list")
    public ResultData<List<JobPostingDTO>> list(
            @RequestParam(name = "industry", required = false) List<String> industry,
            @RequestParam(required = false) Integer experienceLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) List<String> locationCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(name = "sort", defaultValue = "newest") String sort,
            Pageable pageable
    ) {
        if (pageable.getPageSize() == 20) {
            pageable = PageRequest.of(pageable.getPageNumber(), 30);
        }

        Page<JobPostingDTO> dtos = jobPostingService.searchJobPostings(
                industry, experienceLevel, educationLevel, locationCode, keyword, sort, pageable
        );
        return ResultData.success((int) dtos.getTotalElements(), dtos.getContent());
    }

    /**
     * 단일 채용공고 상세 조회
     */
    @GetMapping("/view/{jobPostingId}")
    public JobPostingDTO view(@PathVariable Long jobPostingId) {
        return jobPostingService.getJobPostingDetail(jobPostingId);
    }

    /**
     * 지역
     */
    @GetMapping("/locations")
    public List<LocationDTO> allLocations() {
        return locationRepository.findAllByOrderByIdAsc()
            .stream()
            .map(LocationDTO::fromEntity)        // Entity → DTO 변환
            .collect(Collectors.toList());       // Stream → List
    }

    /**
     * 공고 상세 조회
     */
    @GetMapping("/{jobpostingId}")
    public ResultData<JobPostingDetailDTO> getJobPostingDetails(@PathVariable Long jobpostingId) {
        return jobPostingService.getJobPostingDetails(jobpostingId);
    }
}
