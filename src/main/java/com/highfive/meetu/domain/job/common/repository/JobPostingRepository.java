package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.dashboard.personal.dto.RecommendedJobPostingDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

import java.util.List;
import java.util.Optional;

/**
 * JobPosting (채용 공고) Repository
 * - 인기 공고, 최신 공고, 지원자 많은 공고 등 조회
 * - 추천 공고 조회
 * - 기업별 공고 조회
 * - 대시보드 통계용 쿼리 포함
 */
@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    @Query("""
        SELECT new com.highfive.meetu.domain.dashboard.personal.dto.RecommendedJobPostingDTO(
            c.name,
            jp.title,
            l.fullLocation,
            jp.salaryRange,
            jp.expirationDate,
            jp.keyword
        )
        FROM jobPosting jp
        JOIN jp.company c
        JOIN jp.location l
        WHERE jp.status = 2
        ORDER BY jp.expirationDate ASC
    """)
    List<RecommendedJobPostingDTO> findRecommendedForProfile(
        @Param("profile") Profile profile,
        org.springframework.data.domain.Pageable pageable
    );

  // 특정 기업이 등록한 모든 채용공고 조회
  List<JobPosting> findByCompanyId(Long companyId);
    /**
     * 활성 상태인 공고 중 마감일이 가까운 순으로 10개 조회 (비회원용)
     */
    List<JobPosting> findTop10ByExpirationDateAfterAndStatusOrderByExpirationDateAsc(
            LocalDateTime now, int status
    );

    List<JobPosting> findByCompany_IdInAndStatus(List<Long> companyIds, int status);

    // 특정 기업회원이 속한 기업이 등록한 공고
    List<JobPosting> findByCompany_IdAndStatus(Long companyId, Integer status);

    /**
     * 인기 공고 조회 (조회수 기준 내림차순)
     *
     * @param pageable 페이징 정보
     * @return 인기 공고 목록
     */
    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.viewCount DESC
    """)
    Page<JobPosting> findPopular(Pageable pageable);

    /**
     * 최신 공고 조회 (등록일 기준 내림차순)
     *
     * @param pageable 페이징 정보
     * @return 최신 공고 목록
     */
    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.createdAt DESC
    """)
    Page<JobPosting> findLatest(Pageable pageable);

    /**
     * 지원자 수 기준 인기 공고 조회
     *
     * @param pageable 페이징 정보
     * @return 지원자 수 많은 공고 목록
     */
    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.applyCount DESC
    """)
    Page<JobPosting> findMostApplied(Pageable pageable);

    /**
     * 키워드를 기반으로 추천 공고 조회 (최대 3개 키워드)
     *
     */
    @Query("""
        SELECT DISTINCT j FROM jobPosting j
        WHERE j.status = 2 AND (
            LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword1, '%'))
            OR LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword2, '%'))
            OR LOWER(j.keyword) LIKE LOWER(CONCAT('%', :keyword3, '%'))
        )
        ORDER BY j.createdAt DESC
    """)
    Page<JobPosting> findRecommended(
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2,
        @Param("keyword3") String keyword3,
        Pageable pageable
    );

    /**
     * 외부 고유 ID (jobId)로 공고 조회
     *
     * @param jobId 외부 시스템 연동용 공고 ID
     * @return 채용 공고 Optional
     */
    Optional<JobPosting> findByJobId(String jobId);

    /**
     * 특정 기업의 활성 상태이며 아직 마감되지 않은 채용 공고 목록 조회
     *
     * 조건:
     * - status = 2 (활성 상태)
     * - expirationDate > 현재 시간 (마감되지 않음)
     *
     * 정렬:
     * - 등록일(createdAt) 기준 내림차순 정렬
     *
     * @param companyId 기업 ID
     * @return 마감되지 않은 활성 공고 리스트
     */
    @Query("""
    SELECT j FROM jobPosting j
        WHERE j.company.id = :companyId
          AND j.status = 2
          AND j.expirationDate > CURRENT_TIMESTAMP
        ORDER BY j.createdAt DESC
    """)
    List<JobPosting> findAllActiveAndNotExpiredByCompanyId(@Param("companyId") Long companyId);

    /**
     * 기업 ID와 공고 상태를 기반으로 해당 기업의 공고 개수 조회
     *
     * @param companyId 기업 ID
     * @param status 공고 상태 코드 (ex: 2 = 활성화)
     * @return 공고 수
     */
    @Query("""
        SELECT COUNT(j)
        FROM jobPosting j
        WHERE j.company.id = :companyId
          AND j.status = :status
    """)
    int countByCompanyIdAndStatus(@Param("companyId") Long companyId, @Param("status") int status);

    /**
     * 공고 ID와 상태로 공고 조회
     *
     * @param id 공고 ID
     * @param status 공고 상태
     * @return 채용 공고 Optional
     */
    Optional<JobPosting> findByIdAndStatus(Long id, int status);

    /**
     * 전월 이후 등록된 공고 수 (현재)
     */
    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.createdAt >= :start
    """)
    long countJobPostingsCurrent(@Param("start") LocalDateTime start);

    /**
     * 전월 내 공고 수 (비교용)
     */
    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.createdAt BETWEEN :start AND :end
    """)
    long countJobPostingsPrevious(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * 연도별 월별 공고 수 통계
     */
    @Query("""
        SELECT FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m'), COUNT(j.id)
        FROM jobPosting j
        WHERE YEAR(j.createdAt) = :year
        GROUP BY FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m')
    """)
    List<Object[]> countJobPostingsByMonth(@Param("year") int year);

    /**
     * 직무별 공고 수 (1:N 관계)
     */
    @Query("""
        SELECT jc.jobName, COUNT(j.id)
        FROM jobPosting j
        JOIN j.jobPostingJobCategoryList jpc
        JOIN jpc.jobCategory jc
        GROUP BY jc.jobName
    """)
    List<Object[]> countJobPostingsByCategory();

    /**
     * 전체 공고 수
     */
    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
    """)
    long countTotalJobPostings();

    /**
     * 상태별 공고 수
     */
    @Query("""
        SELECT COUNT(j.id)
        FROM jobPosting j
        WHERE j.status = :status
    """)
    long countJobPostingsByStatus(@Param("status") int status);

    /**
     * 전체 조회수 합계
     */
    @Query("""
        SELECT SUM(j.viewCount)
        FROM jobPosting j
    """)
    long countTotalViews();

    /**
     * 직무 기준 공고 수 (다대다 조인 기반)
     */
    @Query("""
        SELECT jc.jobName, COUNT(j.id)
        FROM jobPosting j
        JOIN j.jobPostingJobCategoryList jpc
        JOIN jpc.jobCategory jc
        GROUP BY jc.jobName
    """)
    List<Object[]> countJobPostingsByJobCategory();

    /**
     * 평균 공고 조회수
     */
    @Query("""
        SELECT AVG(j.viewCount)
        FROM jobPosting j
    """)
    double countAverageViewsPerJobPosting();

    /**
     * 평균 공고 지원자 수
     */
    @Query("""
        SELECT AVG(j.applyCount)
        FROM jobPosting j
    """)
    double countAverageApplicantsPerJobPosting();

    /**
     * 평균 공고 게시 기간 (등록 ~ 마감)
     */
    @Query("""
        SELECT AVG(DATEDIFF(j.expirationDate, j.createdAt))
        FROM jobPosting j
    """)
    double countAveragePostingPeriod();

    /**
     * 상위 5개 기업 공고 수 (활성 상태 기준)
     */
    @Query("""
        SELECT c.name, COUNT(j.id)
        FROM jobPosting j
        JOIN j.company c
        WHERE j.status = :status
        GROUP BY c.name
        ORDER BY COUNT(j.id) DESC
        LIMIT 5
    """)
    List<Object[]> countTopCompaniesJobPostings(@Param("status") int status);

    /**
     * 활성 상태 공고 수 (현재 시간 기준 마감 X)
     */
    @Query("SELECT COUNT(j) FROM jobPosting j WHERE j.status = 2 AND j.expirationDate >= CURRENT_TIMESTAMP")
    long countActiveJobPostings();

    /**
     * 마감된 공고 수 (현재 시간 기준 마감됨)
     */
    @Query("SELECT COUNT(j) FROM jobPosting j WHERE j.status = 2 AND j.expirationDate < CURRENT_TIMESTAMP")
    long countExpiredJobPostings();

    /**
     * 임시저장 상태 공고 수 (status != 2)
     */
    @Query("SELECT COUNT(j) FROM jobPosting j WHERE j.status <> 2")
    long countDraftJobPostings();

    /**
     * 평균 조회수 (모든 상태 포함)
     */
    @Query("SELECT AVG(j.viewCount) FROM jobPosting j")
    Double findAverageViewCount();

    /**
     * 평균 지원자 수 (모든 상태 포함)
     */
    @Query("SELECT AVG(j.applyCount) FROM jobPosting j")
    Double findAverageApplyCount();

    /**
     * 평균 게시 기간 (postingDate ~ expirationDate 기준)
     */
    @Query("SELECT AVG(DATEDIFF(j.expirationDate, j.postingDate)) FROM jobPosting j")
    Double findAveragePostingDays();

    /**
     * 인기 채용 키워드 TOP 15 (쉼표 분리 후 추출)
     */
    @Query(value = """
        SELECT keyword AS keyword, COUNT(*) AS cnt
        FROM (
            SELECT TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(jp.keyword, ',', numbers.n), ',', -1)) AS keyword
            FROM jobPosting jp
            JOIN (
                SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
                UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
                UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
            ) numbers
            ON CHAR_LENGTH(jp.keyword) - CHAR_LENGTH(REPLACE(jp.keyword, ',', '')) >= numbers.n - 1
            WHERE jp.status = 2 AND jp.keyword IS NOT NULL
        ) AS extracted
        GROUP BY keyword
        ORDER BY cnt DESC
        LIMIT 15
        """, nativeQuery = true)
    List<Object[]> findTopKeywordsRaw();

    /**
     * 지역별 채용공고 수 조회
     */
    @Query("""
        SELECT l.province, COUNT(j.id)
        FROM jobPosting j
        JOIN location l ON j.location.id = l.id
        GROUP BY l.province
    """)
    List<Object[]> countJobPostingsByLocation();
}
