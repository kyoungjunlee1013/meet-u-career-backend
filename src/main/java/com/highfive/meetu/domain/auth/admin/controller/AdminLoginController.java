package com.highfive.meetu.domain.auth.admin.controller;

import com.highfive.meetu.domain.auth.admin.service.AdminLoginService;
import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인(Login) 전용 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/auth/")
public class AdminLoginController {

    private final AdminLoginService loginService;

    /**
     * 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
    @PostMapping("/login")
    public ResponseEntity<ResultData<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto) {
        return loginService.login(dto);
    }
}
