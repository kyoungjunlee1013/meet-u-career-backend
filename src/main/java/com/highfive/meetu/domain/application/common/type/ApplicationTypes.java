package com.highfive.meetu.domain.application.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 지원서 관련 Enum 및 컨버터 클래스 모음
 */
public class ApplicationTypes {

    /**
     * 지원 상태 Enum
     */
    public enum Status implements EnumValue {
        PENDING(0),         // 대기
        REVIEWING(1),       // 검토 중
        INTERVIEWING(2),    // 면접 진행
        REJECTED(3),        // 불합격
        CANCELED(4);        // 지원 취소

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown application status value: " + value);
        }
    }

    /**
     * 지원 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}