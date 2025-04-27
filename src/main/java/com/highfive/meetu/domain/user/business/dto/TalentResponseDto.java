package com.highfive.meetu.domain.user.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 인재(이력서) 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentResponseDto {
    /** 식별자 */
    private Long id;
    /** 계정명 */
    private String name;
    /** 직무명 */
    private String title;
    /** 근무 위치 */
    private String location;
    /** 경력 (“경력 N년”) */
    private String experience;
    /** 학력 (“학력 XXX”) */
    private String education;
    /** 보유 스킬 목록 */
    private List<String> skills;
    /** 프로필 이미지 키 */
    private String profileImageKey;
    /** 초과 스킬 수 */
    private Integer moreSkills;
    /** 이력서 설명 */
    private String description;
}
