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

  @CrossOrigin(
      origins = "http://localhost:3000", // 실제 프론트엔드 주소로 변경 필요
      allowCredentials = "true"
  )
  @GetMapping("/{profileId}")
  public ResultData<MyPageDTO> getMyPage(@PathVariable Long profileId) {
    MyPageDTO dto = myPageService.getMyPageInfo(profileId);
    return ResultData.success(1, dto);
  }
}
