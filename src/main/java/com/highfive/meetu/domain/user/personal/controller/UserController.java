package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.UserInfoDTO;
import com.highfive.meetu.domain.user.personal.service.UserService;
import com.highfive.meetu.global.common.response.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResultData<UserInfoDTO> getMyInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getMyInfo(userId);
    }
}
