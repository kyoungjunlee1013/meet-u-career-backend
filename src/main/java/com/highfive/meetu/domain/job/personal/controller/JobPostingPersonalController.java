package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.job.personal.dto.*;
import com.highfive.meetu.domain.job.personal.service.JobPostingPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal/job")
@RequiredArgsConstructor
public class JobPostingPersonalController {

    private final JobPostingPersonalService jobPostingService;
    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.service-url}")
    private String fastapiBaseUrl;

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
    @GetMapping("/{jobPostingId}")
    public ResultData<JobPostingDetailDTO> getJobPostingDetails(
        @PathVariable Long jobPostingId,
        HttpServletRequest request
    ) {
        return jobPostingService.getJobPostingDetails(jobPostingId, request);
    }

    /**
     * 로그인한 회원의 프로필 기반으로 추천 채용공고 리스트 반환(임시 데이터로 테스트 용)
     */
    // @GetMapping("/recommend")
    // public ResultData<RecommendationResponseDTO> recommendJobPostings() {
    //     // 임시 추천 공고 리스트 생성
    //     List<JobRecommendationDTO> jobs = new ArrayList<>();
    //
    //     for (int i = 1; i <= 10; i++) {
    //         JobRecommendationDTO job = new JobRecommendationDTO();
    //         job.setJobPostingId((long) i);
    //         job.setTitle("[더미공고] 백엔드 개발자 채용 " + i);
    //         job.setDescription("Spring Boot 기반 백엔드 개발직무입니다.");
    //         job.setScore(20 + i); // 점수 21 ~ 30 사이
    //
    //         CompanyDTO company = new CompanyDTO();
    //         company.setId(100L + i);
    //         company.setName("쌍용교육 " + i + "팀");
    //         company.setAddress("서울특별시 강남구 테헤란로 " + (100 + i));
    //
    //         job.setCompany(company);
    //         // job.setIndustry("IT/개발");
    //         // job.setSalaryRange("3,000~4,000만원");
    //
    //         jobs.add(job);
    //     }
    //
    //     RecommendationResponseDTO resp = new RecommendationResponseDTO();
    //     resp.setTotal(jobs.size());
    //     resp.setRecommendations(jobs);
    //
    //     return ResultData.success(resp.getTotal(), resp);
    // }

    /**
     * 로그인한 회원의 프로필 기반으로 추천 채용공고 리스트 반환(FAST API 호출)
     */
    @GetMapping("/recommend")
    public ResultData<RecommendationResponseDTO> recommendJobPostings() {
        String url = String.format("%s/recommendations/%d", fastapiBaseUrl, SecurityUtil.getProfileId());
        RecommendationResponseDTO resp =
            restTemplate.getForObject(url, RecommendationResponseDTO.class);

        // FastAPI 호출 성공 시 항상 resp != null
        return ResultData.success(resp.getTotal(), resp);
    }
}
