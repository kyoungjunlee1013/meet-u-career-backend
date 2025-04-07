package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.UserInfoDTO;
import com.highfive.meetu.domain.user.personal.service.UserService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    public ResultData<UserInfoDTO> getMyInfo() {
        System.out.println("---------------------------------------------");
        System.out.println("/me");

        // SecurityUtil을 이용해 accountId 가져오기
        Long accountId = securityUtil.getCurrentAccountId(); // accountId 가져오기
        System.out.println("accountId: " + accountId);

        // SecurityUtil을 이용해 profileId 가져오기
        Long profileId = securityUtil.getProfileIdByAccountId(); // profileId 가져오기
        System.out.println("profileId: " + profileId);
        System.out.println("---------------------------------------------");

        // UserService에서 accountId로 사용자 정보를 가져오기
        return userService.getMyInfo(accountId);
    }
}
