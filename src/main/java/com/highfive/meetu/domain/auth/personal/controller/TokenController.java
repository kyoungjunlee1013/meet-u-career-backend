package com.highfive.meetu.domain.auth.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.personal.dto.RefreshTokenRequestDTO;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    private final JwtProvider jwtProvider;

    @PostMapping("/refresh")
    public ResultData<LoginResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO dto) {
        try {
            Long userId = jwtProvider.parseToken(dto.getRefreshToken());

            String newAccessToken = jwtProvider.generateAccessToken(userId);
            String newRefreshToken = jwtProvider.generateRefreshToken(userId);

            return ResultData.success(1, new LoginResponseDTO(newAccessToken, newRefreshToken));
        } catch (Exception e) {
            return ResultData.fail("Invalid or expired refresh token");
        }
    }
}
