package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    /**
     * 활성 상태인 공고 중 마감일이 가까운 순으로 10개 조회 (비회원용)
     */
    List<JobPosting> findTop10ByExpirationDateAfterAndStatusOrderByExpirationDateAsc(
            LocalDateTime now, int status
    );

    List<JobPosting> findByCompany_IdInAndStatus(List<Long> companyIds, int status);

    // 특정 기업회원이 속한 기업이 등록한 공고
    List<JobPosting> findByCompany_IdAndStatus(Long companyId, Integer status);

}
