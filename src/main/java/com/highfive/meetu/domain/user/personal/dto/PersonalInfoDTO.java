package com.highfive.meetu.domain.user.personal.dto;

import com.highfive.meetu.domain.auth.personal.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 로그인 사용자 정보 DTO
 */
@Data
@AllArgsConstructor
public class PersonalInfoDTO {
    private Long accountId;
    private Long profileId;
    private Role role;
    private String name;            // 사용자 이름
    private String email;           // 관리자일 경우 이메일
    private String companyName;     // 기업회원일 경우 회사명
    private String position;        // 기업회원일 경우 직책
    private String profileImageKey; // 프로필 사진 Key
}
