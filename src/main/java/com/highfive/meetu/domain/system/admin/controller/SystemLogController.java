package com.highfive.meetu.domain.system.admin.controller;

import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.domain.system.admin.dto.SystemLogDto;
import com.highfive.meetu.domain.system.admin.service.SystemLogService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 관리자용 시스템 로그 컨트롤러
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SystemLogController {

  private final SystemLogService systemLogService;

  /**
   * 시스템 로그 전체 조회
   * @return 시스템 로그 DTO 리스트
   */
  // @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER')")
  @GetMapping("/logs")
  public ResultData<List<SystemLogDto>> getAllLogs() {
    List<SystemLogDto> logs = systemLogService.getAllLogs();
    return ResultData.success(logs.size(), logs);
  }
}
