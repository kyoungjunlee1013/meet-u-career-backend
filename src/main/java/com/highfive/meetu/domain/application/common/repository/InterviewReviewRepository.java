package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

  List<InterviewReview> findAllByProfileIdAndStatus(Long profileId, Integer status);

  Optional<InterviewReview> findByIdAndStatus(Long id, Integer status);

  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> findCompaniesWithReviews();

  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> searchCompaniesWithReview(@Param("keyword") String keyword);

  @Query("SELECT r FROM interviewReview r " +
      "JOIN FETCH r.company c " +
      "JOIN FETCH r.jobCategory j " +
      "JOIN FETCH r.application a " +
      "WHERE c.id = :companyId AND r.status = 0")
  List<InterviewReview> findEntitiesByCompanyId(@Param("companyId") Long companyId);

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

  Long countByCompanyIdAndStatus(Long companyId, Integer status);

}
