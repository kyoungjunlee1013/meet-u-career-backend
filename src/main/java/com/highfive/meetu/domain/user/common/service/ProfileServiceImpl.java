package com.highfive.meetu.domain.user.common.service;

import com.highfive.meetu.domain.job.common.repository.JobCategoryRepository;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.user.business.dto.ProfileDto;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 프로필 서비스 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final JobCategoryRepository jobCategoryRepository;

    /**
     * 프로필 목록 조회
     */
    @Override
    public List<ProfileDto> findAll() {
        return profileRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 프로필 단건 조회
     */
    @Override
    public ProfileDto findById(Long id) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        return toDto(profile);
    }

    /**
     * 프로필 생성
     */
    @Override
    public ProfileDto create(ProfileDto dto) {
        Profile profile = Profile.builder()
            .account(accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", dto.getAccountId())))
            .experienceLevel(dto.getExperienceLevel())
            .educationLevel(dto.getEducationLevel())
            .skills(dto.getSkills())
            .desiredSalaryCode(dto.getDesiredSalaryCode())
            .profileImageKey(dto.getProfileImageKey())
            .build();

        if (dto.getLocationId() != null) {
            profile.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", dto.getLocationId())));
        }
        if (dto.getDesiredJobCategoryId() != null) {
            profile.setDesiredJobCategory(jobCategoryRepository.findById(dto.getDesiredJobCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("JobCategory", dto.getDesiredJobCategoryId())));
        }
        if (dto.getDesiredLocationId() != null) {
            profile.setDesiredLocation(locationRepository.findById(dto.getDesiredLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", dto.getDesiredLocationId())));
        }

        Profile saved = profileRepository.save(profile);
        return toDto(saved);
    }

    /**
     * 프로필 수정
     */
    @Override
    public ProfileDto update(Long id, ProfileDto dto) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));

        if (dto.getExperienceLevel() != null) {
            profile.setExperienceLevel(dto.getExperienceLevel());
        }
        if (dto.getEducationLevel() != null) {
            profile.setEducationLevel(dto.getEducationLevel());
        }
        if (dto.getSkills() != null) {
            profile.setSkills(dto.getSkills());
        }
        if (dto.getDesiredSalaryCode() != null) {
            profile.setDesiredSalaryCode(dto.getDesiredSalaryCode());
        }
        if (dto.getProfileImageKey() != null) {
            profile.setProfileImageKey(dto.getProfileImageKey());
        }
        if (dto.getLocationId() != null) {
            profile.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", dto.getLocationId())));
        }
        if (dto.getDesiredJobCategoryId() != null) {
            profile.setDesiredJobCategory(jobCategoryRepository.findById(dto.getDesiredJobCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("JobCategory", dto.getDesiredJobCategoryId())));
        }
        if (dto.getDesiredLocationId() != null) {
            profile.setDesiredLocation(locationRepository.findById(dto.getDesiredLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", dto.getDesiredLocationId())));
        }

        Profile updated = profileRepository.save(profile);
        return toDto(updated);
    }

    /**
     * 프로필 삭제
     */
    @Override
    public void delete(Long id) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        profileRepository.delete(profile);
    }

    /**
     * 엔티티 → DTO 변환 헬퍼
     */
    private ProfileDto toDto(Profile profile) {
        return ProfileDto.builder()
            .id(profile.getId())
            .accountId(profile.getAccount().getId())
            .locationId(profile.getLocation() != null ? profile.getLocation().getId() : null)
            .experienceLevel(profile.getExperienceLevel())
            .educationLevel(profile.getEducationLevel())
            .skills(profile.getSkills())
            .desiredJobCategoryId(
                profile.getDesiredJobCategory() != null ? profile.getDesiredJobCategory().getId() : null)
            .desiredLocationId(
                profile.getDesiredLocation() != null ? profile.getDesiredLocation().getId() : null)
            .desiredSalaryCode(profile.getDesiredSalaryCode())
            .profileImageKey(profile.getProfileImageKey())
            .updatedAt(profile.getUpdatedAt())
            .build();
    }
}
