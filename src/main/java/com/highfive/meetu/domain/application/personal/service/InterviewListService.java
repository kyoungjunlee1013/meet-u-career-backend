package com.highfive.meetu.domain.application.personal.service;


import com.highfive.meetu.domain.application.common.repository.ApplicationQueryRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewListService {

  private final ApplicationQueryRepository applicationQueryRepository;

  public List<InterviewListDTO> getInterviewList(Long profileId) {
    return applicationQueryRepository.findInterviewsWithReviewStatusByProfileId(profileId);
  }
}
