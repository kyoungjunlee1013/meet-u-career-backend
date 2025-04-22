package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long>, JobPostingCustomRepository {
    List<JobPosting> findAllByOrderByCreatedAtDesc();      // 최신순
    List<JobPosting> findAllByOrderByViewCountDesc();     // 인기순(viewCount)
    List<JobPosting> findAllByOrderByApplyCountDesc();    // 추천순(applyCount)

}
