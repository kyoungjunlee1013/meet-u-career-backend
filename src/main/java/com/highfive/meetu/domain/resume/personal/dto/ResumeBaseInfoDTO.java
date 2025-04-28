package com.highfive.meetu.domain.resume.personal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResumeBaseInfoDTO {
    private String name;
    private String email;
    private String phone;
    private String profileImageKey;
    private Long locationId;
    private Long desiredJobCategoryId;
    private String skills;
}