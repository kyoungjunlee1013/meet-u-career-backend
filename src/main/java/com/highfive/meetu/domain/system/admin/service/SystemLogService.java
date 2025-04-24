package com.highfive.meetu.domain.system.admin.service;

import java.util.List;
import com.highfive.meetu.domain.system.admin.dto.SystemLogDto;
/**
 * 관리자용 시스템 로그 서비스 인터페이스
 */
public interface SystemLogService {

  /**
   * 시스템 로그 전체 조회
   * @return 시스템 로그 DTO 리스트
   */
  List<SystemLogDto> getAllLogs();
}
