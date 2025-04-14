package com.highfive.meetu.domain.job.personal.service;



import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    /**
     * 필터 조건에 따른 채용공고 목록 조회
     *
     * @param jobType         채용 형태 (예: "백엔드", "프론트엔드" 등)
     * @param experienceLevel 경력 수준 (정수값, 예: 0은 신입, 1은 경력)
     * @param educationLevel  학력 수준 (정수값)
     * @param locationCode    지역 코드 (문자열 형태로 전달, 예: "101100")
     * @return 조건에 맞는 채용공고 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<JobPostingDTO> filterJobPostings(String jobType, Integer experienceLevel, Integer educationLevel, String locationCode) {
        List<JobPosting> postings = jobPostingRepository.searchByFilters(jobType, experienceLevel, educationLevel, locationCode);
        return postings.stream()
                .map(JobPostingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 기존에 구현된 목록, 상세 조회 메서드도 그대로 둘 수 있습니다.
    @Transactional(readOnly = true)
    public List<JobPostingDTO> getJobPostingList() {
        List<JobPosting> jobPostings = jobPostingRepository.findAllByOrderByCreatedAtDesc();
        return jobPostings.stream()
                .map(JobPostingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobPostingDTO getJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new RuntimeException("채용공고를 찾을 수 없습니다. id: " + jobPostingId));
        return JobPostingDTO.fromEntity(jobPosting);
    }
}