package com.highfive.meetu.domain.auth.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.personal.service.LoginService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인(Login) 전용 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    /**
     * 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
    @PostMapping("/login")
    public ResponseEntity<ResultData<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto) {
        return loginService.login(dto);
    }
}
