package com.highfive.meetu.domain.dashboard.personal.controller;

import com.highfive.meetu.domain.dashboard.personal.dto.MyPageDTO;
import com.highfive.meetu.domain.dashboard.personal.service.MyPageService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;


  @GetMapping("/{accountId}")
  public ResultData<MyPageDTO> getMyPageTest(@PathVariable Long accountId) {
    MyPageDTO dto =myPageService.getMyPageInfo(accountId);
    return ResultData.success(1,dto);
  }

}

