package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 면접 후기 관련 Repository
 */
@Repository
public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

  // applicationId 기준으로 후기 존재 여부 확인
  boolean existsByApplicationId(Long applicationId);

  List<InterviewReview> findByProfileId(Long profileId);

}


