package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {

}
