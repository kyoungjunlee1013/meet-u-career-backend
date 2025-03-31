package com.highfive.meetu.domain.notification.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 알림 관련 Enum 및 컨버터 클래스 모음
 */
public class NotificationTypes {

    /**
     * 알림 유형 Enum
     */
    public enum NotificationType implements EnumValue {
        // 개인회원 알림 (1~10)
        APPLICATION_STATUS(1),         // 지원서 상태 변경
        INTERVIEW_SCHEDULE(2),         // 면접 일정 관련
        JOB_RECOMMENDATION(3),         // 맞춤 채용공고 추천
        OFFER_RECEIVED(4),             // 채용 제안 수신
        MESSAGE_RECEIVED(5),           // 새 메시지 수신

        // 기업회원 알림 (11~20)
        NEW_APPLICATION(11),           // 새로운 지원자 발생
        APPLICATION_WITHDRAWN(12),     // 지원 취소
        JOB_POSTING_EXPIRING(13),      // 공고 만료 임박
        PAYMENT_NOTIFICATION(14),      // 결제/광고 관련 알림
        TALENT_SUGGESTION(15),         // 추천 인재 알림

        // 공통 알림 (21~30)
        SYSTEM_NOTIFICATION(21),       // 시스템 공지/점검
        ACCOUNT_SECURITY(22);          // 계정 보안 관련 알림

        private final int value;

        NotificationType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static NotificationType fromValue(int value) {
            for (NotificationType type : NotificationType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown notification type value: " + value);
        }
    }

    /**
     * 읽음 상태 Enum
     */
    public enum ReadStatus implements EnumValue {
        UNREAD(0),  // 안 읽음
        READ(1);    // 읽음

        private final int value;

        ReadStatus(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static ReadStatus fromValue(int value) {
            for (ReadStatus status : ReadStatus.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown read status value: " + value);
        }
    }

    /**
     * 알림 유형 컨버터
     */
    @Converter
    public static class NotificationTypeConverter extends EnumValueConverter<NotificationType> {
        @Override
        protected NotificationType fromValue(Integer value) {
            return value == null ? null : NotificationType.fromValue(value);
        }
    }

    /**
     * 읽음 상태 컨버터
     */
    @Converter
    public static class ReadStatusConverter extends EnumValueConverter<ReadStatus> {
        @Override
        protected ReadStatus fromValue(Integer value) {
            return value == null ? null : ReadStatus.fromValue(value);
        }
    }
}