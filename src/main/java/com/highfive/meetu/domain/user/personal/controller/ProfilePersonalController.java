package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.domain.user.personal.dto.ProfilePersonalDTO;
import com.highfive.meetu.domain.user.personal.service.ProfilePersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personal/profile")
@RequiredArgsConstructor
public class ProfilePersonalController {

    private final ProfilePersonalService profilePersonalService;

    // 내 프로필 정보 조회 (GET /api/personal/profile/me)
    @GetMapping("/me")
    public ResultData<ProfilePersonalDTO> getMyProfile() {
        //Long profileId = securityUtil.getProfileIdByAccountId();

        Long profileId = 1L;

        ProfilePersonalDTO profile = profilePersonalService.getProfileById(profileId);
        return ResultData.success(1, profile);
    }
}
