package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.personal.dto.InterviewListDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewListService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/interviews")
@RequiredArgsConstructor
public class InterviewListController {

  private final InterviewListService interviewListService;

  @GetMapping
  public ResultData<List<InterviewListDTO>> getInterviewList() {
    Long profileId = SecurityUtil.getProfileId(); // 로그인한 사용자 기준
    List<InterviewListDTO> list = interviewListService.getInterviewList(profileId);
    return ResultData.success(list.size(), list);
  }
}
