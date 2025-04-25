package com.highfive.meetu.domain.system.admin.service;

import com.highfive.meetu.domain.system.common.entity.SystemLog;
import com.highfive.meetu.domain.system.common.repository.SystemLogRepository;
import com.highfive.meetu.domain.system.admin.dto.SystemLogDto;
import com.highfive.meetu.domain.user.common.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리자용 시스템 로그 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class SystemLogServiceImpl implements SystemLogService {

  private final SystemLogRepository systemLogRepository;

  @Override
  public List<SystemLogDto> getAllLogs() {
    List<SystemLog> logs = systemLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    return logs.stream()
        .map(log -> {
          String userId;
          if (log.getAccount() != null) {
            Account account = log.getAccount();
            if (account.getAccountType().equals(Account.AccountType.PERSONAL)) {
              userId = "개인: " + account.getUserId();
            } else {
              userId = "기업: " + account.getUserId();
            }
          } else if (log.getAdmin() != null) {
            userId = "관리자: " + log.getAdmin().getName();
          } else {
            userId = "시스템";
          }
          String type;
          switch (log.getLogType()) {
            case SystemLog.LogType.SECURITY:
              type = "SECURITY";
              break;
            case SystemLog.LogType.TRANSACTION:
              type = "TRANSACTION";
              break;
            case SystemLog.LogType.USER:
              type = "USER";
              break;
            case SystemLog.LogType.ADMIN:
              type = "ADMIN";
              break;
            case SystemLog.LogType.ERROR:
              type = "ERROR";
              break;
            default:
              type = "UNKNOWN";
          }
          return SystemLogDto.builder()
              .id(log.getId())
              .userId(userId)
              .type(type)
              .action(log.getAction())
              .ipAddress(log.getIpAddress())
              .createdAt(log.getCreatedAt())
              .build();
        })
        .collect(Collectors.toList());
  }
}
