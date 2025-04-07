package com.highfive.meetu.domain.auth.personal.service;

import com.highfive.meetu.domain.auth.personal.dto.LoginRequestDTO;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ResultData<LoginResponseDTO> login(LoginRequestDTO dto) {
        Account account = accountRepository.findByEmail(dto.getEmail())
            .orElse(null);

        if (account == null) {
            return ResultData.fail("존재하지 않는 이메일입니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            return ResultData.fail("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(account.getId());
        String refreshToken = jwtProvider.generateRefreshToken(account.getId());

        LoginResponseDTO responseDTO = new LoginResponseDTO(
            accessToken, refreshToken
        );

        return ResultData.success(1, responseDTO);
    }
}
