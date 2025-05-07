package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.dashboard.personal.dto.RecentApplicationDTO;
import com.highfive.meetu.domain.dashboard.personal.dto.ApplicationSummaryDTO;
import com.highfive.meetu.domain.job.business.type.ApplicationStatus;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Application (지원서) Repository
 * - 채용공고별 지원자 목록 조회
 * - 중복 지원 여부 확인
 * - 관리자 대시보드용 통계 쿼리
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationRepositoryCustom {

    /**
     * 특정 프로필의 전체 오퍼 수 조회
     */
    @Query("SELECT COUNT(a) FROM application a WHERE a.profile.id = :profileId")
    int countOffersByProfileId(@Param("profileId") Long profileId);

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
     * 마이페이지에서 최근 지원 이력 조회 (기업명, 제목, 상태)
     */
    @Query("SELECT new com.highfive.meetu.domain.dashboard.personal.dto.RecentApplicationDTO(" +
        "c.name, jp.title, a.status) " +
        "FROM application a " +
        "JOIN a.jobPosting jp " +
        "JOIN jp.company c " +
        "WHERE a.profile.id = :profileId " +
        "ORDER BY a.createdAt DESC")
    List<RecentApplicationDTO> findRecentByProfileId(@Param("profileId") Long profileId);

    /**
     * 마이페이지 상태 요약 (status = 1~4)
     */
    @Query("SELECT new com.highfive.meetu.domain.dashboard.personal.dto.ApplicationSummaryDTO(" +
        "SUM(CASE WHEN a.status = 1 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN a.status = 2 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN a.status = 3 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN a.status = 4 THEN 1 ELSE 0 END)) " +
        "FROM application a " +
        "WHERE a.profile.id = :profileId")
    ApplicationSummaryDTO aggregateStatusSummary(@Param("profileId") Long profileId);

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
    long countAcceptedApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 서류 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2 AND a.createdAt < :start
    """)
    long countRejectedApplicationsPrevious(@Param("start") LocalDateTime start); // 이전 서류 불합격 건수

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
    long countAcceptedApplications(); // 서류 합격 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2
    """)
    long countRejectedApplications(); // 서류 불합격 건수

    /**
     * 지원자 연령대 통계
     */
    @Query(value = """
        SELECT 
            CASE 
                WHEN TIMESTAMPDIFF(YEAR, acc.birthday, CURRENT_DATE()) BETWEEN 10 AND 19 THEN '10대'
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
        ORDER BY FIELD(ageGroup, '10대', '20대', '30대', '40대', '50대', '60대 이상', '기타')
    """, nativeQuery = true)
    List<Object[]> countApplicantsByAgeGroup();

    /**
     * 지원자 수 기준 상위 5개 기업 조회 (서류 상태 관계없이 전체 지원 기준)
     *
     * - application → jobPosting → company 관계로 조인
     * - 기업 이름(c.name)을 기준으로 지원서 수를 COUNT
     * - 지원자 수가 많은 순서로 정렬
     * - 상위 5개만 조회
     *
     * @param pageable 페이징 객체 (size = 5 고정해서 전달)
     * @return Object[] 배열: [0] = 회사 이름 (String), [1] = 지원자 수 (Long)
     */
    @Query("""
        SELECT c.name, COUNT(a.id)
        FROM application a
        JOIN a.jobPosting j
        JOIN j.company c
        GROUP BY c.name
        ORDER BY COUNT(a.id) DESC
    """)
    List<Object[]> countTopCompaniesByApplicants(Pageable pageable);

    /**
     * 시간대별 지원 완료 현황 (1시간 단위)
     * - 지원 상태(status = 0)만 집계
     * - 시간은 '00시', '01시' 형태로 출력됨
     */
    @Query(value = """
        SELECT 
            CONCAT(LPAD(HOUR(createdAt), 2, '0'), '시') AS hourSlot,
            COUNT(*) AS applied
        FROM application
        WHERE status = 0
        GROUP BY hourSlot
        ORDER BY hourSlot
    """, nativeQuery = true)
    List<Object[]> findApplicationTimeStatsHourly();

    /**
     * 지원 추이(오늘 날짜로부터 14일 전까지)
     */
    @Query("""
        SELECT 
            DATE(a.createdAt) AS date,
            COUNT(a.id) AS totalApplications,
            SUM(CASE WHEN a.status = 1 THEN 1 ELSE 0 END) AS acceptedApplications,
            SUM(CASE WHEN a.status = 2 THEN 1 ELSE 0 END) AS rejectedApplications
        FROM application a
        WHERE a.createdAt >= :start AND a.createdAt <= :end AND a.status != 4
        GROUP BY DATE(a.createdAt)
        ORDER BY DATE(a.createdAt)
    """)
    List<Object[]> findDailyApplicationStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 일정 관리 기능 관련
    List<Application> findByProfile_Account_Id(Long accountId);

    /**
     * (Business 대시보드용) 회사 ID 기준으로 공고별 지원자 수 집계
     */
    @Query("""
        SELECT a.jobPosting.id, COUNT(a)
        FROM application a
        WHERE a.jobPosting.company.id = :companyId
        GROUP BY a.jobPosting.id
    """)
    List<Object[]> countApplicationsByJobPostingGrouped(@Param("companyId") Long companyId);

    /**
     * (Business 대시보드용) 회사 ID 기준 지원자 수를 Map<Long, Integer>로 반환
     */
    default Map<Long, Integer> countApplicationsGroupByJobPosting(Long companyId) {
        List<Object[]> results = countApplicationsByJobPostingGrouped(companyId);
        Map<Long, Integer> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((Long) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    /**
     * 특정 공고의 전체 지원자 수 조회
     */
    int countByJobPostingId(Long jobPostingId);

    /**
     * 특정 공고의 상태별 지원자 수 조회
     * - 서류검토중, 서류합격, 서류불합격, 면접완료
     */
    int countByJobPostingIdAndStatus(Long jobPostingId, int status);
}
