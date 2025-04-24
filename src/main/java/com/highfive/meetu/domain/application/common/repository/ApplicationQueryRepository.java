package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.personal.dto.InterviewListDTO;

import java.util.List;

public interface ApplicationQueryRepository {
  List<InterviewListDTO> findInterviewsWithReviewStatusByProfileId(Long profileId);
}
