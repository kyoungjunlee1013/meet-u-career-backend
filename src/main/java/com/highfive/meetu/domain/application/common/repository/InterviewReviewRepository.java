package com.highfive.meetu.domain.application.common.repository;


import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

  // ✅ 특정 프로필 ID 기준 작성된 후기만 조회 (status = 0)
  List<InterviewReview> findAllByProfileIdAndStatus(Long profileId, Integer status);

  // ✅ 단일 후기 조회 (작성된 후기만)
  Optional<InterviewReview> findByIdAndStatus(Long id, Integer status);

  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> findCompaniesWithReviews();

  // ✅ 검색어 기반 기업명 LIKE 조회 LOWER 대소문자 구분 없게 만들어주는
  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> searchCompaniesWithReview(@Param("keyword") String keyword);
}
