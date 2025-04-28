package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.job.common.entity.Location;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

/**
 * 프로필 수정용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateDTO {

    private Long locationId;
    private Long desiredJobCategoryId;
    private String skills;

}
