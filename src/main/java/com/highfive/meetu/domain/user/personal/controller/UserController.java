package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.auth.personal.dto.UserInfoDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    @GetMapping("/me")
    public ResultData<UserInfoDTO> getMyInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Account account = accountRepository.findById(userId).orElse(null);

        if (account == null) {
            return ResultData.fail("사용자 정보를 찾을 수 없습니다.");
        }

        return ResultData.success(1, UserInfoDTO.from(account));
    }
}
