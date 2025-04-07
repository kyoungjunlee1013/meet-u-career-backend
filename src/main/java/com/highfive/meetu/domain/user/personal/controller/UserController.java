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
        System.out.println("---------------------------------");
        Long accountId = securityUtil.getCurrentAccountId();
        System.out.println("accountId: " + accountId);

        Long profileId = securityUtil.getProfileIdByAccountId();
        System.out.println("profileId: " + profileId);
        System.out.println("---------------------------------");

        return userService.getMyInfo(accountId);
    }
}
