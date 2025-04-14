package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long>, JobPostingCustomRepository {
    /**
     * 생성일(createdAt) 기준 내림차순으로 전체 채용공고 목록 조회
     */
    List<JobPosting> findAllByOrderByCreatedAtDesc();

}
