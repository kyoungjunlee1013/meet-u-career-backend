package com.highfive.meetu.domain.portal.service;

import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.domain.portal.dto.MainPageResponse;
import com.highfive.meetu.domain.resume.common.repository.ResumeContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final JobPostingRepository jobPostingRepository;
    private final ResumeContentRepository resumeContentRepository;

    public MainPageResponse getMainContent(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 8);

        List<String> keywords = resumeContentRepository.findTop3KeywordsByUserId(userId);
        String k1 = keywords.size() > 0 ? keywords.get(0) : "";
        String k2 = keywords.size() > 1 ? keywords.get(1) : "";
        String k3 = keywords.size() > 2 ? keywords.get(2) : "";

        return MainPageResponse.builder()
            .recommendations(jobPostingRepository.findRecommended(k1, k2, k3, pageable).map(JobPostingDTO::from).getContent())
            .popular(jobPostingRepository.findPopular(pageable).map(JobPostingDTO::from).getContent())
            .latest(jobPostingRepository.findLatest(pageable).map(JobPostingDTO::from).getContent())
            .mostApplied(jobPostingRepository.findMostApplied(pageable).map(JobPostingDTO::from).getContent())
            .build();
    }
}

