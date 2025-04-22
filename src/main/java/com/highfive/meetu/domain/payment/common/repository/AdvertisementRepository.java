package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    // 특정 공고의 현재 활성화된 광고 조회 (가장 최근 등록된 것)
    @Query("SELECT a FROM advertisement a WHERE a.jobPosting.id = :jobPostingId " +
           "AND a.status = :status " +
           "AND a.startDate <= :now AND a.endDate >= :now " +
           "ORDER BY a.startDate DESC")
    Optional<Advertisement> findActiveAdByJobPostingId(
        @Param("jobPostingId") Long jobPostingId,
        @Param("status") Integer status,
        @Param("now") LocalDateTime now
    );
    
    // 특정 기업의 모든 활성화된 광고 목록 조회
    @Query("SELECT a FROM advertisement a WHERE a.company.id = :companyId " +
           "AND a.status = :status " +
           "AND a.startDate <= :now AND a.endDate >= :now")
    List<Advertisement> findActiveAdsByCompanyId(
        @Param("companyId") Long companyId, 
        @Param("status") Integer status,
        @Param("now") LocalDateTime now
    );
    
    // 특정 공고의 현재 활성화된 광고 또는 미래 광고 조회
    @Query("SELECT a FROM advertisement a WHERE a.jobPosting.id = :jobPostingId " +
           "AND a.status = :status " +
           "AND a.endDate >= :now " +  // 아직 끝나지 않았거나 미래인 광고
           "ORDER BY a.startDate ASC")
    List<Advertisement> findCurrentOrFutureAdsByJobPostingId(
        @Param("jobPostingId") Long jobPostingId,
        @Param("status") Integer status,
        @Param("now") LocalDateTime now
    );

    // 특정 기업의 현재 활성화된 광고 또는 미래 광고 목록 조회
    @Query("SELECT a FROM advertisement a WHERE a.company.id = :companyId " +
           "AND a.status = :status " +
           "AND a.endDate >= :now " +
           "ORDER BY a.startDate ASC")
    List<Advertisement> findCurrentOrFutureAdsByCompanyId(
        @Param("companyId") Long companyId,
        @Param("status") Integer status,
        @Param("now") LocalDateTime now
    );
    
    // 여러 공고에 대한 현재 활성화된 광고 또는 미래 광고 조회
    @Query("SELECT a FROM advertisement a WHERE a.jobPosting.id IN :jobPostingIds " +
           "AND a.status = :status " +
           "AND a.endDate >= :now " +
           "ORDER BY a.jobPosting.id ASC, a.startDate ASC")
    List<Advertisement> findCurrentOrFutureAdsByJobPostingIds(
        @Param("jobPostingIds") List<Long> jobPostingIds,
        @Param("status") Integer status,
        @Param("now") LocalDateTime now
    );

}
