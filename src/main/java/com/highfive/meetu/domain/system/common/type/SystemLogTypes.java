package com.highfive.meetu.domain.system.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 시스템 로그 관련 Enum 및 컨버터 클래스 모음
 * - 프로젝트에서 중요 기능에 대해서만 로그를 남기기 위한 타입 정의
 * - 불필요한 로그는 피하고 보안 및 중요 활동만 선택적으로 기록
 * - 데이터베이스에는 INT 타입으로 저장됨
 */
public class SystemLogTypes {

    /**
     * 로그 유형 Enum
     * - 프로젝트 상황에 맞는 핵심 로그 유형만 정의
     * - 이 유형들은 LogUtil 클래스의 각 메서드에 대응됨
     * - 데이터베이스에는 정수값으로 저장됨
     */
    public enum LogType implements EnumValue {
        /**
         * 보안 관련 로그
         * 사용 시점: 로그인/로그아웃, 비밀번호 변경, 계정 잠금/해제 등 인증 관련 활동
         * 예시: "로그인 성공", "비밀번호 재설정", "계정 잠금"
         */
        SECURITY(0),

        /**
         * 거래 관련 로그
         * 사용 시점: 결제 상태 변경, 환불 처리 등 금전적 거래 상태 변화
         * 예시: "결제 상태 변경: 시도 → 성공", "환불 처리 완료"
         */
        TRANSACTION(1),

        /**
         * 사용자 활동 로그
         * 사용 시점: 중요 사용자 활동 (DB에 이미 기록되는 일반 CRUD 제외)
         * 예시: "이력서 공개 설정 변경", "회원 탈퇴 요청"
         */
        USER(2),

        /**
         * 관리자 활동 로그
         * 사용 시점: 관리자 권한을 사용한 모든 활동 (감사 추적 목적)
         * 예시: "회원 상태 변경", "게시글 강제 삭제", "공고 검수 승인"
         */
        ADMIN(3),

        /**
         * 오류 로그
         * 사용 시점: 시스템 오류, 예외 발생, API 연동 실패 등 오류 상황
         * 예시: "결제 처리 중 오류", "API 연동 실패", "시스템 예외 발생"
         */
        ERROR(4);

        private final int value;

        LogType(int value) {
            this.value = value;
        }

        /**
         * 데이터베이스에 저장될 정수값 반환
         * @return 지정된 정수값
         */
        @Override
        public int getValue() {
            return this.value;
        }

        /**
         * 문자열 표현 반환 (로깅, 디버깅용)
         * @return Enum의 문자열 표현
         */
        public String getStringValue() {
            return this.name();
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         * @param value 데이터베이스에 저장된 정수값
         * @return 대응하는 LogType enum 상수
         * @throws IllegalArgumentException 유효하지 않은 값인 경우
         */
        public static LogType fromValue(int value) {
            for (LogType type : LogType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown log type value: " + value);
        }

        /**
         * 문자열로부터 Enum 인스턴스 찾기
         * @param value 문자열 표현
         * @return 대응하는 LogType enum 상수
         * @throws IllegalArgumentException 유효하지 않은 값인 경우
         */
        public static LogType fromString(String value) {
            try {
                return LogType.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown log type: " + value);
            }
        }
    }

    /**
     * 로그 모듈 Enum
     * - 프로젝트의 핵심 모듈만 정의
     * - 각 모듈은 기능 영역을 구분하여 로그 분석을 용이하게 함
     * - 데이터베이스에는 정수값으로 저장됨
     */
    public enum ModuleType implements EnumValue {
        /**
         * 인증/인가 모듈
         * 관련 기능: 로그인, 로그아웃, 회원가입, 비밀번호 관리
         */
        AUTH(0),

        /**
         * 사용자 관리 모듈
         * 관련 기능: 프로필 관리, 계정 설정, 회원 상태 관리
         */
        USER(1),

        /**
         * 기업 관리 모듈
         * 관련 기능: 기업 정보 관리, 기업 검수, 기업 계정 관리
         */
        COMPANY(2),

        /**
         * 채용공고 모듈
         * 관련 기능: 공고 등록/수정/삭제, 공고 검색, 공고 관리
         */
        JOB_POSTING(3),

        /**
         * 이력서 관리 모듈
         * 관련 기능: 이력서 작성/수정/삭제, 이력서 공개 설정
         */
        RESUME(4),

        /**
         * 지원서 관리 모듈
         * 관련 기능: 입사지원, 지원서 관리, 제안 관리
         */
        APPLICATION(5),

        /**
         * 결제 모듈
         * 관련 기능: 결제 처리, 환불, 결제 내역 관리
         */
        PAYMENT(6),

        /**
         * 관리자 모듈
         * 관련 기능: 관리자 계정 관리, 시스템 설정, 통계 관리
         */
        ADMIN(7);

        private final int value;

        ModuleType(int value) {
            this.value = value;
        }

        /**
         * 데이터베이스에 저장될 정수값 반환
         * @return 지정된 정수값
         */
        @Override
        public int getValue() {
            return this.value;
        }

        /**
         * 문자열 표현 반환 (로깅, 디버깅용)
         * @return Enum의 문자열 표현
         */
        public String getStringValue() {
            return this.name();
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         * @param value 데이터베이스에 저장된 정수값
         * @return 대응하는 ModuleType enum 상수
         * @throws IllegalArgumentException 유효하지 않은 값인 경우
         */
        public static ModuleType fromValue(int value) {
            for (ModuleType type : ModuleType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown module type value: " + value);
        }

        /**
         * 문자열로부터 Enum 인스턴스 찾기
         * @param value 문자열 표현
         * @return 대응하는 ModuleType enum 상수
         * @throws IllegalArgumentException 유효하지 않은 값인 경우
         */
        public static ModuleType fromString(String value) {
            try {
                return ModuleType.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown module type: " + value);
            }
        }
    }

    /**
     * 로그 유형 컨버터
     * - LogType Enum을 데이터베이스의 INT 컬럼으로 자동 변환
     */
    @Converter(autoApply = true)
    public static class LogTypeConverter extends EnumValueConverter<LogType> {
        @Override
        protected LogType fromValue(Integer value) {
            return value == null ? null : LogType.fromValue(value);
        }
    }

    /**
     * 로그 모듈 컨버터
     * - ModuleType Enum을 데이터베이스의 INT 컬럼으로 자동 변환
     */
    @Converter(autoApply = true)
    public static class ModuleTypeConverter extends EnumValueConverter<ModuleType> {
        @Override
        protected ModuleType fromValue(Integer value) {
            return value == null ? null : ModuleType.fromValue(value);
        }
    }
}