package com.highfive.meetu.domain.application.personal.controller;


import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/interview-reviews")
@RequiredArgsConstructor
public class InterviewReviewPersonalController {

  private final InterviewReviewPersonalService interviewReviewPersonalService;

  @GetMapping
  public ResultData<List<InterviewReviewPersonalDTO>> getList(@RequestParam Long profileId) {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findAllByProfileId(profileId);
    return ResultData.success(list.size(), list);
  }

  @GetMapping("/{reviewId}")
  public ResultData<InterviewReviewPersonalDTO> getDetail(@PathVariable Long reviewId) {
    return ResultData.success(1, interviewReviewPersonalService.findById(reviewId));
  }

}
