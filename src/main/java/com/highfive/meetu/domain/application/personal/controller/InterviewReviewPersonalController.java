package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewApplicationDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 개인 사용자 면접 후기 컨트롤러
 */
@RestController
@RequestMapping("/api/personal/interview-reviews")
@RequiredArgsConstructor
public class InterviewReviewPersonalController {

  private final InterviewReviewPersonalService interviewReviewPersonalService;

  /**
   * (가정) 외부에서 Application 리스트를 주입받는 경우 테스트용
   * 실제로는 queryRepository 또는 다른 팀원의 로직과 연결 필요
   */
  @PostMapping("/reviewable-list")
  public ResultData<List<InterviewReviewApplicationDTO>> getReviewableApplications(
      @RequestBody List<Application> applications) {
    List<InterviewReviewApplicationDTO> result =
        interviewReviewPersonalService.toDTOList(applications);
    return ResultData.success(result.size(), result);
  }

  @GetMapping
  public ResultData<List<InterviewReviewDTO>> getMyReviews() {
    Long profileId = SecurityUtil.getProfileId(); // 현재 로그인한 사용자 기준
    List<InterviewReviewDTO> reviews = interviewReviewPersonalService.getReviewsByProfileId(profileId);
    return ResultData.success(reviews.size(), reviews);

  }
}

