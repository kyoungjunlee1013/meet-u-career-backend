package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // 기업회원 공고 관리 - 목록 페이지
    List<JobPosting> findByCompanyId(Long companyId);

    // industry 자동완성 기능
    @Query("SELECT DISTINCT j.industry FROM jobPosting j " +
            "WHERE j.industry LIKE %:keyword% " +
            "AND j.status = 2 " +
            "ORDER BY j.industry ASC")
    List<String> findDistinctIndustriesByKeyword(@Param("keyword") String keyword);
}
