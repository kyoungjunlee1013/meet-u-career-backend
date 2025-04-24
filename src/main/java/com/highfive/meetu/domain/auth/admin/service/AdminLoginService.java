package com.highfive.meetu.domain.auth.admin.service;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.personal.service.RefreshTokenService;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.domain.user.common.repository.AdminRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 로그인 처리 서비스
 * - 토큰 발급
 * - Redis RefreshToken 저장
 */
@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<ResultData<LoginResponseDTO>> login(LoginRequestDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급
        String accessToken = jwtProvider.generateAccessToken(admin.getId());
        String refreshToken = jwtProvider.generateRefreshToken(admin.getId());

        // Redis에 refreshToken 저장
        refreshTokenService.saveRefreshToken(
            admin.getId(),
            refreshToken,
            jwtProvider.getRefreshTokenExpiration()
        );

        // 응답 세팅
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
            .httpOnly(true)
            .secure(false) // 개발 환경에서는 false
            .path("/")
            .sameSite("Strict")
            .maxAge(Duration.ofMillis(jwtProvider.getAccessTokenExpiration()))
            .build();

        // refreshToken 쿠키
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .sameSite("Strict")
            .maxAge(Duration.ofMillis(jwtProvider.getRefreshTokenExpiration()))
            .build();

        LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken, refreshToken);

        return ResponseEntity.ok()
            .headers(headers -> {
                headers.add("Set-Cookie", accessTokenCookie.toString());
                headers.add("Set-Cookie", refreshTokenCookie.toString());
            })
            .body(ResultData.success(1, responseDTO));
    }
}
