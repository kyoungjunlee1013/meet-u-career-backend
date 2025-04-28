package com.highfive.meetu.domain.dashboard.personal.controller;

import com.highfive.meetu.domain.dashboard.personal.dto.MyPageDTO;
import com.highfive.meetu.domain.dashboard.personal.service.MyPageService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;

  @GetMapping()
  public ResultData<MyPageDTO> getMyPage() {
    MyPageDTO dto = myPageService.getMyPageInfo();
    return ResultData.success(1, dto);
  }
}
