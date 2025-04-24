package com.highfive.meetu.domain.user.common.controller;

import com.highfive.meetu.domain.auth.personal.type.Role;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.domain.user.personal.dto.PersonalInfoDTO;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.security.LoginUser;
import com.highfive.meetu.global.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.highfive.meetu.global.common.response.ErrorCode.*;

/**
 * 공통 로그인 사용자 정보 API
 * - 개인/기업/관리자 공용
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    @GetMapping("/me")
    public ResultData<PersonalInfoDTO> getMyInfo(@LoginUser CustomUserPrincipal user) {
        if (user == null) {
            return ResultData.fail("로그인 정보가 없습니다.");
        }

        Long accountId = user.getAccountId();
        Long profileId = user.getProfileId(); // 기업/관리자일 경우 null 가능
        Role role = user.getRole();

        if (accountId == null || role == null) {
            return ResultData.fail(USER_NOT_FOUND);
        }

        Account account = accountRepository.findById(accountId)
            .orElse(null);

        if (account == null) {
            return ResultData.fail(USER_NOT_FOUND);
        }

        String name = account.getName();
        String profileImageKey = null;

        if (profileId != null) {
            Profile profile = profileRepository.findById(profileId)
                .orElse(null);

            if (profile == null) {
                return ResultData.fail(PROFILE_NOT_FOUND);
            }

            profileImageKey = profile.getProfileImageKey();
        }

        PersonalInfoDTO dto = new PersonalInfoDTO(
            accountId,
            profileId,
            role,
            name,
            profileImageKey
        );

        return ResultData.success(1, dto);
    }
}
