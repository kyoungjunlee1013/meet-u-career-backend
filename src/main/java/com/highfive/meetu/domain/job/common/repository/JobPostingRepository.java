package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.viewCount DESC
    """)
    Page<JobPosting> findPopular(Pageable pageable);

    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.createdAt DESC
    """)
    Page<JobPosting> findLatest(Pageable pageable);

    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.applyCount DESC
    """)
    Page<JobPosting> findMostApplied(Pageable pageable);

    @Query("""
        SELECT DISTINCT j FROM jobPosting j
        WHERE j.status = 2 AND (LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword1, '%'))
        OR LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword2, '%'))
        OR LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword3, '%')))
        ORDER BY j.createdAt DESC
    """)
    Page<JobPosting> findRecommended(
        @Param("keyword1") String k1,
        @Param("keyword2") String k2,
        @Param("keyword3") String k3,
        Pageable pageable
    );
    Optional<JobPosting> findByJobId(String jobId);
}
