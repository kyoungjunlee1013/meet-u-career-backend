package com.highfive.meetu.domain.user.personal.dto;


import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePersonalDTO {

    private Long profileId;
    private Long accountId;
    private String name;
    private String email;
    private String phone;

    private String profileImageKey;        // 프로필 이미지 S3 Key
    private String desiredJobCategoryName; // 희망 직무 이름
    private String desiredLocationName;    // 희망 근무 지역 이름
    private Integer experienceLevel;       // 경력 수준
    private Integer educationLevel;        // 학력 수준
    private Integer desiredSalaryCode;     // 희망 연봉 코드
    private String skills;                 // 기술 스택
    private String profileImageUrl;

    public static ProfilePersonalDTO fromEntities(Profile profile, Account account) {
        return ProfilePersonalDTO.builder()
                .profileId(profile.getId())
                .accountId(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .phone(account.getPhone())
                .profileImageKey(profile.getProfileImageKey())
                .desiredJobCategoryName(profile.getDesiredJobCategory() != null
                        ? profile.getDesiredJobCategory().getJobName() : null)
                .desiredLocationName(profile.getDesiredLocation() != null
                        ? profile.getDesiredLocation().getProvince() : null)
                .experienceLevel(profile.getExperienceLevel())
                .educationLevel(profile.getEducationLevel())
                .desiredSalaryCode(profile.getDesiredSalaryCode())
                .skills(profile.getSkills())
                .build();
    }
}
