package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;

import java.util.List;

/**
 * 채용공고 커스텀 레포지토리 (QueryDSL 사용)
 */
public interface JobPostingRepositoryCustom {
    /**
     * skills 리스트를 keyword에 포함하는 채용공고 조회 (랜덤 정렬, limit 설정)
     *
     * @param skills 키워드 리스트
     * @param limit 최대 조회 개수
     * @return 채용공고 리스트
     */
    List<JobPosting> findBySkillsInKeyword(List<String> skills, int limit);
}