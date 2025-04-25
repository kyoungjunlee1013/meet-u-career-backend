package com.highfive.meetu.domain.user.business.talents;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 프로필 데이터 전송 객체 (DTO)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    /** 식별자 */
    private Long id;

    /** 계정 ID (필수) */
    @NotNull(message = "계정 ID는 필수입니다.")
    private Long accountId;

    /** 현재 위치 ID (선택) */
    private Long locationId;

    /** 경력 레벨 */
    private Integer experienceLevel;

    /** 학력 레벨 */
    private Integer educationLevel;

    /** 보유 스킬 목록 */
    private String skills;

    /** 희망 직무 카테고리 ID */
    private Long desiredJobCategoryId;

    /** 희망 지역 ID */
    private Long desiredLocationId;

    /** 희망 연봉 코드 */
    private Integer desiredSalaryCode;

    /** 프로필 이미지 키 */
    private String profileImageKey;

    /** 마지막 수정 일자 */
    private LocalDateTime updatedAt;
}
