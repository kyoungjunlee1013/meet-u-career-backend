package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.user.personal.dto.PersonalEmailRequestDTO;
import com.highfive.meetu.domain.user.personal.dto.PersonalSignUpRequestDTO;
import com.highfive.meetu.domain.user.personal.service.PersonalAccountService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/personal")
public class PersonalAccountController {
    private final PersonalAccountService accountService;

    // 이메일(아이디) 중복 체크
    @PostMapping("/check/email")
    public ResultData<Boolean> findByEmail(@RequestBody PersonalEmailRequestDTO dto) {
        boolean result = accountService.findByEmail(dto.getEmail());
        return ResultData.success(result ? 1 : 0, result);
    }

    // 개인회원 회원가입
    @PostMapping("/signup")
    public ResultData<PersonalSignUpRequestDTO> signupBusiness(@RequestBody PersonalSignUpRequestDTO dto) {
        PersonalSignUpRequestDTO result = accountService.save(dto);
        return ResultData.success((result != null) ? 1 : 0, result);
    }
}
