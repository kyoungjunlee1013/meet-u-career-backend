package com.highfive.meetu.domain.dashboard.personal.controller;

import com.highfive.meetu.domain.dashboard.personal.dto.MyPageDTO;
import com.highfive.meetu.domain.dashboard.personal.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;


  @GetMapping("/{accountId}")
  public MyPageDTO getMyPageTest(@PathVariable Long accountId) {
    return myPageService.getMyPageInfo(accountId);
  }

}

