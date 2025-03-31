package com.highfive.meetu.domain.coverletter.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 자기소개서 관련 Enum 및 컨버터 클래스 모음
 */
public class CoverLetterTypes {

    /**
     * 자기소개서 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),   // 활성
        DRAFT(1),    // 임시저장
        DELETED(2);  // 삭제 대기

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
            throw new IllegalArgumentException("Unknown cover letter status value: " + value);
        }
    }

    /**
     * 자기소개서 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}