package com.highfive.meetu.domain.auth.personal.service;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
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
public class LoginService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<ResultData<LoginResponseDTO>> login(LoginRequestDTO dto) {
        Account account = accountRepository.findByUserId(dto.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급
        String accessToken = jwtProvider.generateAccessToken(account.getId());
        String refreshToken = jwtProvider.generateRefreshToken(account.getId());

        // Redis에 refreshToken 저장
        refreshTokenService.saveRefreshToken(
            account.getId(),
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
