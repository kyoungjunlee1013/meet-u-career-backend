package com.highfive.meetu.domain.application.common.repository;


import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

  // ✅ 특정 프로필 ID 기준 작성된 후기만 조회 (status = 0)
  List<InterviewReview> findAllByProfileIdAndStatus(Long profileId, Integer status);

  // ✅ 단일 후기 조회 (작성된 후기만)
  Optional<InterviewReview> findByIdAndStatus(Long id, Integer status);
}
