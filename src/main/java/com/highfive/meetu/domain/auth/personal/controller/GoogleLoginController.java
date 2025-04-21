package com.highfive.meetu.domain.auth.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.personal.service.GoogleLoginService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;

    @PostMapping("/google/login")
    public ResultData<LoginResponseDTO> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        LoginResponseDTO loginResponse = googleLoginService.login(idToken);
        return ResultData.success(1, loginResponse);
    }
}
