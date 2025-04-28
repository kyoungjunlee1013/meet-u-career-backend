package com.highfive.meetu.domain.dashboard.personal.service;

import com.highfive.meetu.domain.dashboard.personal.dto.ProfileInfoDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    public ProfileInfoDTO getProfileInfo() {
        Long accountId = SecurityUtil.getAccountId();
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("Account not found"));

        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("Profile not found"));

        return ProfileInfoDTO.build(account, profile);
    }

    public void updateProfileByProfileId(ProfileInfoDTO dto) {
        Long accountId = SecurityUtil.getAccountId();

        // 프로필 조회
        Profile profile = profileRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("Profile not found"));

        // 계정 조회 (Profile 엔티티에 Account가 포함되어 있음)
        Account account = profile.getAccount();
        if (account == null) throw new NotFoundException("Account not found");

        // 계정 정보 직접 설정
        account.setName(dto.getName());
        account.setPhone(dto.getPhone());

        // 프로필 정보 직접 설정
        profile.setExperienceLevel(dto.getExperienceLevel());
        profile.setEducationLevel(dto.getEducationLevel());
        profile.setSkills(dto.getSkills());
        profile.setDesiredSalaryCode(dto.getDesiredSalaryCode());
        profile.setProfileImageKey(dto.getProfileImageUrl());

        // 변경 사항 저장
        accountRepository.save(account);
        profileRepository.save(profile);
    }
}