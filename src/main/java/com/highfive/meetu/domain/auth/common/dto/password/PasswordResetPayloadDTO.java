package com.highfive.meetu.domain.auth.common.dto.password;

import lombok.*;

/**
 * 비밀번호 재설정 JWT 토큰으로부터 추출한 페이로드 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetPayloadDTO {
  // 토큰에 저장된 사용자 이메일
  private String email;
  // 토큰에 저장된 6자리 인증 코드
  private String code;
}
