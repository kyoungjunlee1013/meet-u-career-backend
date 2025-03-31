package com.highfive.meetu.domain.system.common.entity;

import com.highfive.meetu.domain.system.common.type.SystemLogTypes.LogType;
import com.highfive.meetu.domain.system.common.type.SystemLogTypes.ModuleType;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 시스템 로그 엔티티 (SystemLog)
 *
 * 목적:
 * - 중요 시스템 활동, 사용자 보안 관련 활동, 관리자 작업을 추적
 * - 일반 CRUD 작업은 로깅하지 않고, 중요 활동만 선택적으로 로깅
 * - 보안 감사, 사용자 활동 분석, 문제 추적에 활용
 *
 * 저장 최적화:
 * - logType과 module 필드는 INT 타입으로 DB에 저장되어 조회 성능 및 공간 효율성 향상
 * - JPA Enum 컨버터를 통해 Java 코드에서는 의미있는 Enum으로 사용
 *
 * 연관관계:
 * - Account(1) : SystemLog(N) - SystemLog가 주인, @JoinColumn 사용
 * - Admin(1) : SystemLog(N) - SystemLog가 주인, @JoinColumn 사용
 */
@Entity(name = "systemLog")
@Table(
        indexes = {
                @Index(name = "idx_log_accountId", columnList = "accountId"),    // 계정별 로그 조회 최적화
                @Index(name = "idx_log_adminId", columnList = "adminId"),        // 관리자별 로그 조회 최적화
                @Index(name = "idx_log_logType", columnList = "logType"),        // 로그 유형별 필터링 최적화
                @Index(name = "idx_log_module", columnList = "module"),          // 모듈별 필터링 최적화
                @Index(name = "idx_log_createdAt", columnList = "createdAt")     // 날짜별 조회 최적화
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "admin"})  // 순환 참조 방지를 위해 연관 엔티티 제외
public class SystemLog extends BaseEntity {

    /**
     * 로그를 남긴 사용자 계정
     * - 일반 회원 활동 로그에 사용
     * - 회원이 아닌 경우 null 가능
     * - FetchType.LAZY: 로그 조회 시 계정 정보는 필요할 때만 로딩하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    /**
     * 로그를 남긴 관리자 계정
     * - 관리자 활동 로그에 사용
     * - 관리자가 아닌 경우 null 가능
     * - FetchType.LAZY: 로그 조회 시 관리자 정보는 필요할 때만 로딩하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private Admin admin;

    /**
     * 로그 유형 (데이터베이스에 INT로 저장됨)
     *
     * 정의된 유형:
     * - SECURITY(0): 보안 관련 로그 (로그인, 비밀번호 변경, 계정 잠금/해제 등)
     * - TRANSACTION(1): 거래 관련 로그 (결제 상태 변경, 환불 처리 등)
     * - USER(2): 사용자 활동 로그 (중요 설정 변경, 회원 탈퇴 요청 등)
     * - ADMIN(3): 관리자 활동 로그 (회원 상태 변경, 게시글 강제 삭제, 공고 검수 승인 등)
     * - ERROR(4): 오류 로그 (시스템 오류, 예외 발생, API 연동 실패 등)
     *
     * 자동 변환:
     * - SystemLogTypes.LogTypeConverter에 의해 INT ↔ Enum 자동 변환됨
     */
    @Column(nullable = false)
    private LogType logType;

    /**
     * 로그가 발생한 모듈 (데이터베이스에 INT로 저장됨)
     *
     * 정의된 모듈:
     * - AUTH(0): 인증/인가 모듈 (로그인, 로그아웃, 회원가입, 비밀번호 관리)
     * - USER(1): 사용자 관리 모듈 (프로필 관리, 계정 설정, 회원 상태 관리)
     * - COMPANY(2): 기업 관리 모듈 (기업 정보 관리, 기업 검수, 기업 계정 관리)
     * - JOB_POSTING(3): 채용공고 모듈 (공고 등록/수정/삭제, 공고 검색, 공고 관리)
     * - RESUME(4): 이력서 관리 모듈 (이력서 작성/수정/삭제, 이력서 공개 설정)
     * - APPLICATION(5): 지원서 관리 모듈 (입사지원, 지원서 관리, 제안 관리)
     * - PAYMENT(6): 결제 모듈 (결제 처리, 환불, 결제 내역 관리)
     * - ADMIN(7): 관리자 모듈 (관리자 계정 관리, 시스템 설정, 통계 관리)
     *
     * 자동 변환:
     * - SystemLogTypes.ModuleTypeConverter에 의해 INT ↔ Enum 자동 변환됨
     */
    @Column(nullable = false)
    private ModuleType module;

    /**
     * 수행한 작업 내용
     * - 간결하고 명확하게 작성 (예: "로그인 성공", "회원 상태 변경: 활성 → 정지")
     * - 민감한 개인정보는 포함하지 않음
     * - 로그 분석 시 검색 및 필터링 용이하도록 일관된 형식 권장
     */
    @Column(length = 255, nullable = false)
    private String action;

    /**
     * 접속한 IP 주소
     * - 보안 추적 및 악의적인 활동 감지에 활용
     * - IPv4 및 IPv6 주소를 모두 저장할 수 있도록 충분한 길이 확보
     * - 내부 시스템 로그인 경우 "SYSTEM" 또는 서버 IP 기록 가능
     */
    @Column(length = 50, nullable = false)
    private String ipAddress;

}