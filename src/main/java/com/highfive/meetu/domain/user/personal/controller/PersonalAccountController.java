package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.user.personal.dto.PersonalEmailRequestDTO;
import com.highfive.meetu.domain.user.personal.dto.PersonalSignUpRequestDTO;
import com.highfive.meetu.domain.user.personal.service.PersonalAccountService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/account")
public class PersonalAccountController {
    private final PersonalAccountService accountService;

    // 이메일(아이디) 중복 체크
    @PostMapping("/check/userid")
    public ResultData<Boolean> findByUserId(String userId) {
        boolean result = accountService.findByUserId(userId);

        if (result) {
            return ResultData.of(1, "이미 존재하는 아이디입니다.", null);
        } else {
            return ResultData.of(0, "사용 가능한 아이디입니다.", null);
        }
    }

    // 이메일(아이디) 중복 체크
    @PostMapping("/check/email")
    public ResultData<Boolean> findByEmail(@RequestBody PersonalEmailRequestDTO dto) {
        boolean result = accountService.findByEmail(dto.getEmail());

        if (result) {
            return ResultData.of(1, "이미 존재하는 이메일입니다.", null);
        } else {
            return ResultData.of(0, "사용 가능한 이메일입니다.", null);
        }
    }

    // 개인회원 회원가입
    @PostMapping("/signup")
    public ResultData<PersonalSignUpRequestDTO> signupBusiness(@RequestBody PersonalSignUpRequestDTO dto) {
        PersonalSignUpRequestDTO result = accountService.save(dto);

        if (result != null) {
            return ResultData.success(1, result);
        } else {
            return ResultData.fail();
        }
    }
}
