package com.highfive.meetu.domain.auth.common.dto.password;

import lombok.*;
import java.time.LocalDate;

/**
 * 비밀번호 찾기 요청 DTO
 * 이메일, 이름, 생년월일을 받아 사용자 존재 여부를 확인합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindPasswordRequestDTO {
  // 사용자 이메일
  private String email;
  // 사용자 이름
  private String name;
  // 사용자 생년월일 (yyyy-MM-dd)
  private LocalDate birthday;
}
