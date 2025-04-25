package com.highfive.meetu.domain.system.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 시스템 로그 조회용 DTO
 *
 * 프론트엔드에 전달할 로그 요약 정보
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogDto {

  private Long id;   // 로그 PK

  private String userId;   // 사용자/관리자 ID

  private String type;     // 로그 유형 (SECURITY, TRANSACTION, USER, ADMIN, ERROR)

  private String action;   // 수행한 작업 내용

  private String ipAddress; // 접속한 IP 주소

  private LocalDateTime createdAt; // 생성일시
}
