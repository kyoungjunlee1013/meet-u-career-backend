package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

/**
 * [기업회원] 채용공고별 지원자 목록 조회 DTO
 * - 기업 관리자 페이지에서 지원자 리스트 조회 시 사용
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponseDTO {

    private Long applicationId;     // 지원서 ID
    private String applicantName;   // 지원자 이름
    private String email;           // 지원자 이메일
    private String phone;           // 지원자 연락처
    private String position;        // 채용 공고 제목
    private String appliedDate;     // 지원일
    private Integer status;         // 상태

    /**
     * Application 엔티티를 기반으로 DTO 생성
     *
     * @param application 지원서 엔티티
     * @return ApplicantResponseDTO 객체
     */
    public static ApplicantResponseDTO fromEntity(Application application) {
        Profile profile = application.getProfile();  // 지원자의 프로필 정보

        return ApplicantResponseDTO.builder()
            .applicationId(application.getId())                                 // 지원서 ID
            .applicantName(profile.getAccount().getName())                      // 이름은 Account에서 가져옴
            .email(profile.getAccount().getEmail())                             // 이메일
            .phone(profile.getAccount().getPhone())                             // 연락처
            .position(application.getJobPosting().getTitle())                   // 채용 공고 제목
            .appliedDate(application.getCreatedAt().toLocalDate().toString())   // 지원일
            .status(application.getStatus())                                    // 상태
            .build();
    }
}
