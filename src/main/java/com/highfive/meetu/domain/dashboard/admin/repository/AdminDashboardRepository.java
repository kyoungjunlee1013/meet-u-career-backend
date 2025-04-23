package com.highfive.meetu.domain.dashboard.admin.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface AdminDashboardRepository extends JpaRepository<JobPosting, Long> {

    @Query("""
        SELECT COUNT(a.id)
        FROM account a
        WHERE a.createdAt >= :start
    """)
    long countUsersCurrent(@Param("start") String start);

    @Query("""
        SELECT COUNT(a.id)
        FROM account a
        WHERE a.createdAt BETWEEN :start AND :end
    """)
    long countUsersPrevious(@Param("start") String start, @Param("end") String end);

    @Query("""
        SELECT COUNT(c.id)
        FROM company c
        WHERE c.createdAt >= :start
    """)
    long countCompaniesCurrent(@Param("start") String start);

    @Query("""
        SELECT COUNT(c.id)
        FROM company c
        WHERE c.createdAt BETWEEN :start AND :end
    """)
    long countCompaniesPrevious(@Param("start") String start, @Param("end") String end);

    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.createdAt >= :start
    """)
    long countJobPostingsCurrent(@Param("start") String start);

    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.createdAt BETWEEN :start AND :end
    """)
    long countJobPostingsPrevious(@Param("start") String start, @Param("end") String end);

    @Query("""
        SELECT COUNT(p.id)
        FROM communityPost p
        WHERE p.createdAt >= :start
    """)
    long countPostsCurrent(@Param("start") String start);

    @Query("""
        SELECT COUNT(p.id)
        FROM communityPost p
        WHERE p.createdAt BETWEEN :start AND :end
    """)
    long countPostsPrevious(@Param("start") String start, @Param("end") String end);

    @Query("""
        SELECT FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m'), COUNT(a.id)
        FROM account a
        WHERE YEAR(a.createdAt) = :year
        GROUP BY FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m')
    """)
    List<Object[]> countUsersByMonth(@Param("year") int year);

    @Query("""
        SELECT a.accountType, COUNT(a.id)
        FROM account a
        GROUP BY a.accountType
    """)
    List<Object[]> countUsersByType();

    @Query("""
        SELECT FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m'), COUNT(j.id)
        FROM jobPosting j
        WHERE YEAR(j.createdAt) = :year
        GROUP BY FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m')
    """)
    List<Object[]> countJobPostingsByMonth(@Param("year") int year);

    @Query("""
        SELECT jc.jobName, COUNT(j.id)
        FROM jobPosting j
        JOIN j.jobPostingJobCategoryList jpc
        JOIN jpc.jobCategory jc
        GROUP BY jc.jobName
    """)
    List<Object[]> countJobPostingsByCategory();

    @Query("""
        SELECT j.title, j.company.name, j.applyCount
        FROM jobPosting j
        WHERE j.status = :status
        ORDER BY j.applyCount DESC
    """)
    List<Object[]> countPopularJobPostings(@Param("status") int status);

    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
    """)
    long countTotalJobPostings();  // 채용 공고 총 수

    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.status = :status
    """)
    long countJobPostingsByStatus(@Param("status") int status);  // 채용 공고 상태별 수

    @Query("""
        SELECT COUNT(c.id)
        FROM company c
    """)
    long countParticipatingCompanies();  // 참여 기업 수

    @Query("""
        SELECT SUM(j.viewCount)
        FROM jobPosting j
    """)
    long countTotalViews();  // 총 조회수

    @Query("""
        SELECT jc.jobName, COUNT(j.id)
        FROM jobPosting j
        JOIN j.jobPostingJobCategoryList jpc
        JOIN jpc.jobCategory jc
        GROUP BY jc.jobName
    """)
    List<Object[]> countJobPostingsByJobCategory();

    @Query("""
        SELECT j.status, COUNT(j.id)
        FROM jobPosting j
        GROUP BY j.status
    """)
    List<Object[]> countJobPostingsByStatus();  // 채용공고 상태별 통계 (활성, 마감, 임시저장)

    @Query("""
        SELECT AVG(j.viewCount)
        FROM jobPosting j
    """)
    double countAverageViewsPerJobPosting();  // 공고당 평균 조회수

    @Query("""
        SELECT AVG(j.applyCount)
        FROM jobPosting j
    """)
    double countAverageApplicantsPerJobPosting();  // 공고당 평균 지원자 수

    @Query("""
        SELECT AVG(DATEDIFF(j.expirationDate, j.createdAt))
        FROM jobPosting j
    """)
    double countAveragePostingPeriod();  // 평균 공고 게시 기간

    @Query("""
        SELECT c.name, COUNT(j.id)
        FROM jobPosting j
        JOIN j.company c
        WHERE j.status = :status
        GROUP BY c.name
        ORDER BY COUNT(j.id) DESC
        LIMIT 5
    """)
    List<Object[]> countTopCompaniesJobPostings(@Param("status") int status);  // 채용공고 상위 5개 기업

    @Query(value = """
        SELECT j.keyword, COUNT(j.id), 
               (COUNT(j.id) - COALESCE(LAG(COUNT(j.id)) OVER (ORDER BY YEAR(j.createdAt), MONTH(j.createdAt)), 0)) * 100.0 / COALESCE(LAG(COUNT(j.id)) OVER (ORDER BY YEAR(j.createdAt), MONTH(j.createdAt)), 1) AS growthRate
        FROM jobPosting j
        WHERE j.status = :status
        GROUP BY j.keyword
        ORDER BY growthRate DESC
    """, nativeQuery = true)
    List<Object[]> countPopularJobKeywords(@Param("status") int status);  // 인기 채용 키워드 조회

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 0 AND a.createdAt < :start
    """)
    long countTotalApplicationsPrevious(@Param("start") String start); // 이전 지원 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 1 AND a.createdAt < :start
    """)
    long countInProgressApplicationsPrevious(@Param("start") String start); // 이전 진행 중인 지원 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2 AND a.createdAt < :start
    """)
    long countAcceptedApplicationsPrevious(@Param("start") String start); // 이전 합격한 지원 건수

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 3 AND a.createdAt < :start
    """)
    long countRejectedApplicationsPrevious(@Param("start") String start); // 이전 불합격한 지원 건수

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
    long countInProgressApplications(); // 서류 합격

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 2
    """)
    long countAcceptedApplications(); // 면접 합격

    @Query("""
        SELECT COUNT(a.id)
        FROM application a
        WHERE a.status = 3
    """)
    long countRejectedApplications(); // 최종 합격

    // 지원자 연령대별 조회 (Native Query로 변경)
    @Query(value = """
        SELECT 
            CASE 
                WHEN TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE()) BETWEEN 20 AND 29 THEN '20대'
                WHEN TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE()) BETWEEN 30 AND 39 THEN '30대'
                WHEN TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE()) BETWEEN 40 AND 49 THEN '40대'
                WHEN TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE()) BETWEEN 50 AND 59 THEN '50대'
                WHEN TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE()) >= 60 THEN '60대 이상'
                ELSE '기타'
            END AS ageGroup,
            COUNT(a.id)
        FROM application a
        GROUP BY ageGroup
    """, nativeQuery = true)
    List<Object[]> countApplicantsByAgeGroup();

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

    // 지원 시간 분석을 위한 쿼리 (Native SQL)
    @Query(value = """
        SELECT 
            CASE 
                WHEN HOUR(a.createdAt) BETWEEN 0 AND 6 THEN '오전'
                WHEN HOUR(a.createdAt) BETWEEN 7 AND 11 THEN '1-3시'
                WHEN HOUR(a.createdAt) BETWEEN 12 AND 15 THEN '4-7시'
                WHEN HOUR(a.createdAt) BETWEEN 16 AND 19 THEN '8-11시'
                ELSE '12-3시'
            END AS timeRange,
            AVG(TIMESTAMPDIFF(HOUR, a.createdAt, a.completedAt)) AS completionTime,
            AVG(TIMESTAMPDIFF(HOUR, a.createdAt, a.cancelledAt)) AS cancellationTime,
            AVG(TIMESTAMPDIFF(HOUR, a.createdAt, a.modifiedAt)) AS modificationTime
        FROM application a
        GROUP BY timeRange
    """, nativeQuery = true)
    List<Object[]> countApplyTime();


}
