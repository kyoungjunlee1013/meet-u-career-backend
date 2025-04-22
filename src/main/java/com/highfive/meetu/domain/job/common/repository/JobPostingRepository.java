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
     * 지원자 수 기준으로 인기 공고 조회 (지원자 수 내림차순)
     *
     * @param pageable 페이징 정보
     * @return 지원 많은 공고 목록
     */
    @Query("""
        SELECT j FROM jobPosting j
        WHERE j.status = 2
        ORDER BY j.applyCount DESC
    """)
    Page<JobPosting> findMostApplied(Pageable pageable);

    /**
     * 추천 공고 조회 (키워드 기준 검색, 최신순 정렬)
     *
     * @param k1 추천 키워드1
     * @param k2 추천 키워드2
     * @param k3 추천 키워드3
     * @param pageable 페이징 정보
     * @return 추천 공고 목록
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
        @Param("keyword1") String k1,
        @Param("keyword2") String k2,
        @Param("keyword3") String k3,
        Pageable pageable
    );

    /**
     * JobId를 기반으로 공고 조회
     *
     * @param jobId 외부 시스템 연동용 공고 ID
     * @return 채용 공고 Optional
     */
    Optional<JobPosting> findByJobId(String jobId);

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
}
