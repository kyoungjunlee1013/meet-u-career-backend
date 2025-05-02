package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.job.common.entity.Location;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {

    private Long id;
    private Long accountId;

    private Long locationId;
    private Long desiredLocationId;
    private Long desiredJobCategoryId;

    private Integer experienceLevel;
    private Integer educationLevel;
    private String skills;

    private Integer desiredSalaryCode;
    private String profileImageKey;

    public Profile toEntity() {
        return Profile.builder()
                .id(id)
                .account(Account.builder().id(accountId).build()) // FK 연결
                .location(locationId != null ? Location.builder().id(locationId).build() : null)
                .desiredLocation(desiredLocationId != null ? Location.builder().id(desiredLocationId).build() : null)
                .desiredJobCategory(desiredJobCategoryId != null ? JobCategory.builder().id(desiredJobCategoryId).build() : null)
                .experienceLevel(experienceLevel)
                .educationLevel(educationLevel)
                .skills(skills)
                .desiredSalaryCode(desiredSalaryCode)
                .profileImageKey(profileImageKey)
                .build();
    }
}
