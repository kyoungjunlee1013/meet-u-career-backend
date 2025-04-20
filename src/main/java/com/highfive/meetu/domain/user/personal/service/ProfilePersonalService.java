package com.highfive.meetu.domain.user.personal.service;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.domain.user.personal.dto.ProfilePersonalDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfilePersonalService {

    private final ProfileRepository profileRepository;

    /**
     * profileId로 Profile + Account 정보 DTO 반환
     */
    public ProfilePersonalDTO getProfileById(Long profileId) {
        // 1. 프로필 조회 (account 연관관계 fetch)
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

        // 2. 연관된 Account 조회
        Account account = profile.getAccount();
        if (account == null) {
            throw new NotFoundException("연결된 계정 정보를 찾을 수 없습니다.");
        }

        // 3. Profile + Account 정보 DTO로 변환
        return ProfilePersonalDTO.fromEntities(profile, account);
    }
}
