package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.auth.personal.type.Role;
import com.highfive.meetu.domain.user.personal.dto.PersonalInfoDTO;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.security.LoginUser;
import com.highfive.meetu.global.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자(개인회원) API
 */
@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
public class PersonalController {

    @PreAuthorize("hasRole('USER')") // 개인회원만 접근 허용
    @GetMapping("/me")
    public ResultData<PersonalInfoDTO> myPage(@LoginUser CustomUserPrincipal user) {
        if (user == null) {
            return ResultData.fail("로그인 정보가 없습니다.");
        }

        Long accountId = user.getAccountId();
        Long profileId = user.getProfileId();
        Role role = user.getRole();

        if (accountId == null || role == null) {
            return ResultData.fail("사용자 정보를 찾을 수 없습니다.");
        }

        PersonalInfoDTO dto = new PersonalInfoDTO(accountId, profileId, role);

        return ResultData.success(1, dto);
    }
}
