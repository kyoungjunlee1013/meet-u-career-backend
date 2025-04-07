package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 면접 후기 저장/조회용 Repository
 * - 기본 CRUD 및 간단한 조건 검색용으로 사용
 * - 복잡한 조회는 QueryDSL로 별도 분리
 */
@Repository
public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {
  List<InterviewReview> findAllByProfile_Id(Long profileId);

  // applicationId 기준으로 후기 존재 여부 확인
  boolean existsByApplicationId(Long applicationId);

}




