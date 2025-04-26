package com.highfive.meetu.domain.user.admin.dto;

import com.highfive.meetu.domain.user.common.entity.Admin;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAccountDTO {

  private Long id;
  private String name;
  private String email;
  private String password; // 생성/수정 시만 사용
  private Integer role;
  private String createdAt;

  public static AdminAccountDTO build(Admin admin) {
    return AdminAccountDTO.builder()
        .id(admin.getId())
        .name(admin.getName())
        .email(admin.getEmail())
        .role(admin.getRole())
        .createdAt(admin.getCreatedAt() != null ? admin.getCreatedAt().toString() : null)
        .build();
  }
}
