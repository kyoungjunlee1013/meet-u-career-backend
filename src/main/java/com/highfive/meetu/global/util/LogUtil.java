package com.highfive.meetu.global.util;

import com.highfive.meetu.domain.system.common.entity.SystemLog;
import com.highfive.meetu.domain.system.common.repository.SystemLogRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Admin;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
     * 클라이언트 IP 주소 추출
     *
     * @param req 현재 HTTP 요청
     * @return 클라이언트 IP 주소
     */
    private static String extractIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 보안 관련 시스템 로그 저장
     *
     * @param account 개인 계정 정보
     * @param module  모듈 코드
     * @param action  작업 내용
     * @param req     HTTP 요청
     */
    public void logSecurity(Account account, int module, String action, HttpServletRequest req) {
        SystemLog log = SystemLog.builder()
                .account(account)
                .logType(SystemLog.LogType.SECURITY)
                .module(module)
                .action(action)
                .ipAddress(extractIp(req))
                .build();
        systemLogRepository.save(log);
    }

    /**
     * 관리자 활동 로그 저장
     *
     * @param admin  관리자 계정 정보
     * @param module 모듈 코드
     * @param action 작업 내용
     * @param req    HTTP 요청
     */
    public void logSecurity(Admin admin, int module, String action, HttpServletRequest req) {
        SystemLog log = SystemLog.builder()
                .admin(admin)
                .logType(SystemLog.LogType.ADMIN)
                .module(module)
                .action(action)
                .ipAddress(extractIp(req))
                .build();
        systemLogRepository.save(log);
    }
}
