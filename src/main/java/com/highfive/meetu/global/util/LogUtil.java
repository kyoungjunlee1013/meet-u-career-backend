package com.highfive.meetu.global.util;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.domain.system.common.entity.SystemLog;
import com.highfive.meetu.domain.system.common.repository.SystemLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 시스템 로그 생성 유틸리티
 * - 핵심 기능에 대한 최소한의 로그만 생성하도록 설계
 * - 데이터베이스에 이미 기록되는 일반 CRUD 작업은 로깅하지 않음
 */
@Component
@RequiredArgsConstructor
public class LogUtil {

    private final SystemLogRepository systemLogRepository;

    /**
     * 보안 관련 로그 (로그인, 로그아웃, 비밀번호 변경 등)
     * LogType.SECURITY(0) 값으로 저장됨
     *
     * @param account 계정 정보
     * @param module 관련 모듈 (데이터베이스에 INT로 저장)
     * @param action 수행한 작업 내용
     */
    public void logSecurity(Account account, Integer module, String action) {
        SystemLog log = SystemLog.builder()
                .account(account)
                .logType(SystemLog.LogType.SECURITY)  // 데이터베이스에 0으로 저장
                .module(module)             // 데이터베이스에 INT로 저장
                .action(action)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 관리자 활동 로그 (필수 감사 추적용)
     * LogType.ADMIN(3) 값으로 저장됨
     *
     * @param admin 관리자 정보
     * @param module 관련 모듈 (데이터베이스에 INT로 저장)
     * @param action 수행한 관리 작업 내용
     */
    public void logAdmin(Admin admin, Integer module, String action) {
        SystemLog log = SystemLog.builder()
                .admin(admin)
                .logType(SystemLog.LogType.ADMIN)     // 데이터베이스에 3으로 저장
                .module(module)             // 데이터베이스에 INT로 저장
                .action(action)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 결제/트랜잭션 상태 변경 로그
     * LogType.TRANSACTION(1) 값으로 저장됨
     *
     * @param account 계정 정보
     * @param action 트랜잭션 작업 내용
     */
    public void logTransaction(Account account, String action) {
        SystemLog log = SystemLog.builder()
                .account(account)
                .logType(SystemLog.LogType.TRANSACTION)  // 데이터베이스에 1로 저장
                .module(SystemLog.Module.PAYMENT)    // 데이터베이스에 6으로 저장
                .action(action)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 중요 사용자 활동 로그 (민감한 정보 변경 등)
     * LogType.USER(2) 값으로 저장됨
     *
     * @param account 계정 정보
     * @param module 관련 모듈 (데이터베이스에 INT로 저장)
     * @param action 수행한 작업 내용
     */
    public void logUserAction(Account account, Integer module, String action) {
        SystemLog log = SystemLog.builder()
                .account(account)
                .logType(SystemLog.LogType.USER)      // 데이터베이스에 2로 저장
                .module(module)             // 데이터베이스에 INT로 저장
                .action(action)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 시스템 오류 로그
     * LogType.ERROR(4) 값으로 저장됨
     *
     * @param module 오류가 발생한 모듈 (데이터베이스에 INT로 저장)
     * @param action 오류 설명
     * @param e 발생한 예외
     */
    public void logError(Integer module, String action, Exception e) {
        String errorMessage = action + ": " + e.getMessage();
        // 메시지가 너무 길면 잘라내기
        if (errorMessage.length() > 255) {
            errorMessage = errorMessage.substring(0, 252) + "...";
        }

        SystemLog log = SystemLog.builder()
                .logType(SystemLog.LogType.ERROR)     // 데이터베이스에 4로 저장
                .module(module)             // 데이터베이스에 INT로 저장
                .action(errorMessage)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 시스템 일반 로그 (특정 계정과 관련 없는 시스템 작업)
     * LogType.USER(2) 값으로 저장됨
     *
     * @param module 관련 모듈 (데이터베이스에 INT로 저장)
     * @param action 시스템 작업 내용
     */
    public void logSystem(Integer module, String action) {
        SystemLog log = SystemLog.builder()
                .logType(SystemLog.LogType.USER)      // 데이터베이스에 2로 저장
                .module(module)             // 데이터베이스에 INT로 저장
                .action(action)
                .ipAddress(getClientIp())
                .build();

        systemLogRepository.save(log);
    }

    /**
     * 현재 요청의 IP 주소 추출
     *
     * @return 클라이언트 IP 주소
     */
    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ipAddress = request.getHeader("X-Forwarded-For");

                if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
                return ipAddress;
            }
        } catch (Exception e) {
            // 예외 발생 시 기본값 반환
        }
        return "0.0.0.0"; // 기본값
    }
}