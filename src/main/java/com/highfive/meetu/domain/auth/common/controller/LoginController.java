package com.highfive.meetu.domain.auth.common.controller;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.common.service.login.LoginService;
import com.highfive.meetu.domain.auth.personal.service.RefreshTokenService;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.util.LogUtil;
import com.highfive.meetu.domain.system.common.entity.SystemLog;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Account.AccountType;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.AdminRepository;

import com.highfive.meetu.infra.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.highfive.meetu.global.util.CookieUtil.extractRefreshToken;

/**
 * 로그인(Login) 전용 API 컨트롤러
 */
@Tag(name = "00-login-controller", description = "로그인/토큰 발급 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final LogUtil logUtil;
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * 개인 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
    @Operation(summary = "개인회원 로그인 API")
    @PostMapping("/personal/auth/login")
    public ResponseEntity<ResultData<LoginResponseDTO>> personalLogin(@RequestBody LoginRequestDTO dto,
            HttpServletRequest request) {
        Account account = accountRepository
                .findByUserIdAndAccountType(dto.getUserId(), AccountType.PERSONAL)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 아이디입니다."));
        ResponseEntity<ResultData<LoginResponseDTO>> response = loginService.userLogin(dto);
        logUtil.logSecurity(account, SystemLog.Module.AUTH, "login personal", request);
        return response;
    }

    /**
     * 기업 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
    @Operation(summary = "기업회원 로그인 API")
    @PostMapping("/business/auth/login")
    public ResponseEntity<ResultData<LoginResponseDTO>> businessLogin(@RequestBody LoginRequestDTO dto,
            HttpServletRequest request) {
        Account account = accountRepository
                .findByUserIdAndAccountType(dto.getUserId(), AccountType.BUSINESS)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 아이디입니다."));
        ResponseEntity<ResultData<LoginResponseDTO>> response = loginService.businessLogin(dto);
        logUtil.logSecurity(account, SystemLog.Module.AUTH, "login business", request);
        return response;
    }

    /**
     * 관리자 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
    @Operation(summary = "관리자 로그인 API")
    @PostMapping("/admin/auth/login")
    public ResponseEntity<ResultData<LoginResponseDTO>> adminLogin(@RequestBody LoginRequestDTO dto,
            HttpServletRequest request) {
        Admin admin = adminRepository
                .findByEmail(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 이메일입니다."));
        ResponseEntity<ResultData<LoginResponseDTO>> response = loginService.adminLogin(dto);
        logUtil.logSecurity(admin, SystemLog.Module.AUTH, "login admin", request);
        return response;
    }

    /**
     * 로그아웃 처리
     * - Redis의 RefreshToken 삭제
     * - AccessToken, RefreshToken 쿠키 삭제
     */
    @PostMapping("/auth/logout")
    public ResultData<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null) {
            return ResultData.fail("RefreshToken이 없습니다. 다시 로그인해주세요.");
        }

        Long accountId;
        try {
            accountId = jwtProvider.parseToken(refreshToken);
        } catch (Exception e) {
            return ResultData.fail("토큰이 유효하지 않습니다. 다시 로그인해주세요.");
        }

        // Redis에서 RefreshToken 삭제
        refreshTokenService.deleteRefreshToken(accountId);

        // 쿠키 삭제 (maxAge=0)
        ResponseCookie deleteAccessTokenCookie = ResponseCookie.from("accessToken", "")
            .httpOnly(true)
            .secure(false) // 운영(prod)에서는 true
            .path("/")
            .sameSite("Strict")
            .maxAge(0)
            .build();

        ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .sameSite("Strict")
            .maxAge(0)
            .build();

        response.addHeader("Set-Cookie", deleteAccessTokenCookie.toString());
        response.addHeader("Set-Cookie", deleteRefreshTokenCookie.toString());

        return ResultData.success(1, "로그아웃이 완료되었습니다.");
    }
}
