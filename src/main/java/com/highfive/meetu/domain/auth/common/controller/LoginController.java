package com.highfive.meetu.domain.auth.common.controller;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.common.service.login.LoginService;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.util.LogUtil;
import com.highfive.meetu.domain.system.common.entity.SystemLog;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Account.AccountType;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.AdminRepository;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인(Login) 전용 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final LogUtil logUtil;
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;

    /**
     * 개인 로그인 요청
     * - AccessToken, RefreshToken 발급
     */
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
}
