package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.auth.personal.type.Role;
import com.highfive.meetu.domain.user.personal.service.UserService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.security.LoginUser;
import com.highfive.meetu.global.security.CustomUserPrincipal;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자(개인회원) API
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @PreAuthorize("hasRole('USER')") // 개인회원만 접근 허용
    @GetMapping("/me")
    public ResultData<?> myPage(@LoginUser CustomUserPrincipal user) {
        Long accountId = user.getAccountId();
        Long profileId = user.getProfileId();
        Role role = user.getRole();

        System.out.println("accountId: " + accountId);
        System.out.println("profileId: " + profileId);
        System.out.println("role: " + role);

        return ResultData.success(1, "ok");
    }
}
