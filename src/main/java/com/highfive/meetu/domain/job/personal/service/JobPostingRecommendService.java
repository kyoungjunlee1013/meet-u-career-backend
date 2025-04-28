package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepositoryCustom;
import com.highfive.meetu.domain.job.personal.dto.RecommendedJobPostingDTO;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 개인회원 추천 채용공고 서비스
 */
@Service
@RequiredArgsConstructor
public class JobPostingRecommendService {

    private final ProfileRepository profileRepository;
    private final JobPostingRepositoryCustom jobPostingRepositoryCustom;

    /**
     * 로그인한 회원의 프로필 skills를 기반으로 추천 채용공고 조회
     */
    @Transactional(readOnly = true)
    public List<RecommendedJobPostingDTO> recommendByProfileSkills() {
        Long accountId = SecurityUtil.getAccountId();
        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

        if (profile.getSkills() == null || profile.getSkills().isBlank()) {
            return Collections.emptyList();
        }

        List<String> skills = Arrays.stream(profile.getSkills().split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .collect(Collectors.toList());

        List<JobPosting> jobPostings = jobPostingRepositoryCustom.findBySkillsInKeyword(skills, 5);
        return jobPostings.stream()
            .map(RecommendedJobPostingDTO::fromEntity)
            .collect(Collectors.toList());
    }
}