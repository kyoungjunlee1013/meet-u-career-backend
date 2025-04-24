package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application (지원서) Repository
 * - 채용공고별 지원자 목록 조회
 * - 중복 지원 여부 확인
 * - 관리자 대시보드용 통계 쿼리
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationRepositoryCustom {

    /**
     * 특정 채용공고에 대한 전체 지원자 목록 조회 (삭제 제외)
     */
    @Query("""
        SELECT a
        FROM application a
        JOIN FETCH a.profile p
        WHERE a.jobPosting.id = :jobPostingId
          AND a.status != 4
    """)
    List<Application> findAllByJobPostingId(@Param("jobPostingId") Long jobPostingId);

    /**
     * 중복 지원 여부 확인
     */
    boolean existsByProfileAndJobPosting(Profile profile, JobPosting jobPosting);

    /**
     * 대시보드 통계
     */
    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 0 AND a.createdAt < :start
    """)
    long countTotalApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 총 지원 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 1 AND a.createdAt < :start
    """)
    long countInProgressApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 서류 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2 AND a.createdAt < :start
    """)
    long countAcceptedApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 면접 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 3 AND a.createdAt < :start
    """)
    long countRejectedApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 최종 불합격 건수

    /**
     * 대시보드 통계
     */
    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 0
    """)
    long countTotalApplications(); // 총 지원 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 1
    """)
    long countInProgressApplications(); // 서류 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2
    """)
    long countAcceptedApplications(); // 면접 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 3
    """)
    long countRejectedApplications(); // 최종 합격 건수

    /**
     * 지원자 연령대 통계
     */
    @Query(value = """
        SELECT 
            CASE 
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) BETWEEN 20 AND 29 THEN '20대'
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) BETWEEN 30 AND 39 THEN '30대'
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) BETWEEN 40 AND 49 THEN '40대'
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) BETWEEN 50 AND 59 THEN '50대'
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) >= 60 THEN '60대 이상'
                ELSE '기타'
            END AS ageGroup,
            COUNT(a.id)
        FROM application a
        JOIN profile p ON a.profileId = p.id
        JOIN account acc ON p.accountId = acc.id
        GROUP BY ageGroup
    """, nativeQuery = true)
    List<Object[]> countApplicantsByAgeGroup();

    /**
     * 서류합격 기준 기업별 지원자 수 통계 (TOP 5)
     */
    @Query("""
        SELECT c.name, COUNT(a.id)
        FROM application a
        JOIN a.jobPosting j
        JOIN j.company c
        WHERE a.status = 2
        GROUP BY c.name
        ORDER BY COUNT(a.id) DESC
    """)
    List<Object[]> countTopCompaniesByApplicants();

    /**
     * 시간대별 지원 현황 (오전/오후 등)
     */
    @Query(value = """
        SELECT 
            CASE 
                WHEN HOUR(createdAt) < 12 THEN '오전'
                WHEN HOUR(createdAt) BETWEEN 13 AND 15 THEN '1-3시'
                WHEN HOUR(createdAt) BETWEEN 16 AND 19 THEN '4-7시'
                WHEN HOUR(createdAt) BETWEEN 20 AND 23 THEN '8-11시'
                WHEN HOUR(createdAt) = 12 THEN '12-3시'
                ELSE '3시 이후'
            END AS timeSlot,
            SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS applied,
            SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS canceled,
            SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) AS modified
        FROM application
        GROUP BY timeSlot
        ORDER BY FIELD(timeSlot, '오전', '1-3시', '4-7시', '8-11시', '12-3시', '3시 이후')
        """, nativeQuery = true)
    List<Object[]> findApplicationTimeStatsRaw();
}
