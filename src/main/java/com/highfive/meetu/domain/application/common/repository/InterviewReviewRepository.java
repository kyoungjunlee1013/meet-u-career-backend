package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import java.util.List;

/**
 * 면접 후기 관련 Repository
 */
@Repository
public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

    // applicationId 기준으로 후기 존재 여부 확인
    boolean existsByApplicationId(Long applicationId); // 지원서 기준 중복 작성 방지

    List<InterviewReview> findByProfileId(Long profileId);

    /**
   * 특정 사용자(profileId)가 작성한 후기 중 status가 일치하는 목록 조회
   */
  List<InterviewReview> findAllByProfileIdAndStatus(Long profileId, Integer status);

  /**
   * 후기 ID와 상태값으로 단건 후기 조회
   */
  Optional<InterviewReview> findByIdAndStatus(Long id, Integer status);

  /**
   * 면접 후기가 등록된 기업 목록을 후기 수 기준으로 정렬해서 반환
   * - InterviewCompanySummaryDTO로 매핑
   */
  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> findCompaniesWithReviews();

  /**
   * 키워드로 기업명을 검색하여 면접 후기가 존재하는 기업 목록 반환
   * - 대소문자 구분 없이 검색
   */
  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> searchCompaniesWithReview(@Param("keyword") String keyword);

  /**
   * 특정 기업(companyId)에 등록된 면접 후기 전체 조회 (상세 조회용)
   * - company, jobCategory, application 즉시 로딩 (JOIN FETCH)
   */
  @Query("SELECT r FROM interviewReview r " +
      "JOIN FETCH r.company c " +
      "JOIN FETCH r.jobCategory j " +
      "JOIN FETCH r.application a " +
      "WHERE c.id = :companyId AND r.status = 0")
  List<InterviewReview> findEntitiesByCompanyId(@Param("companyId") Long companyId);

  /**
   * 최신 등록된 후기 중 상위 10건만 반환 (createdAt 내림차순)
   * - 직접 InterviewReviewPersonalDTO로 매핑
   */
  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO(" +
      "r.id, c.name, j.jobName, r.interviewYearMonth, r.rating, r.createdAt, " +
      "r.careerLevel, r.difficulty, r.interviewType, r.interviewParticipants, r.hasFrequentQuestions, " +
      "r.questionsAsked, r.interviewTip, r.result, a.id, c.id, j.id, r.status) " +
      "FROM interviewReview r " +
      "JOIN r.company c " +
      "JOIN r.jobCategory j " +
      "JOIN r.application a " +
      "WHERE r.status = 0 " +
      "ORDER BY r.createdAt DESC")
  List<InterviewReviewPersonalDTO> findTop10RecentReviews(Pageable pageable);

  /**
   * 특정 회사에 등록된 활성 후기 개수 반환
   */
  Long countByCompanyIdAndStatus(Long companyId, Integer status);

  /**
   * 특정 회사의 후기 전체 개수 반환 (status 상관없이)
   */
  @Query("""
      SELECT COUNT(ir) FROM interviewReview ir
      WHERE ir.company.id = :companyId
  """)
  int countByCompanyId(Long companyId);

  Optional<InterviewReview> findByApplicationId(Long applicationId);
}
