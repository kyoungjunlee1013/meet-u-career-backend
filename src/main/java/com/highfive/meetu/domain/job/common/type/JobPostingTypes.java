package com.highfive.meetu.domain.job.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 채용공고 관련 Enum 및 컨버터 클래스 모음
 */
public class JobPostingTypes {

    /**
     * 공고 상태 Enum
     */
    public enum Status implements EnumValue {
        INACTIVE(0),      // 비활성
        PENDING(1),       // 승인 대기
        ACTIVE(2);        // 활성

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
            throw new IllegalArgumentException("Unknown job posting status value: " + value);
        }
    }

    /**
     * 마감 형식 Enum
     */
    public enum CloseType implements EnumValue {
        DEADLINE(1),       // 마감일
        UPON_HIRING(2);    // 채용 시 마감

        private final int value;

        CloseType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static CloseType fromValue(int value) {
            for (CloseType type : CloseType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown close type value: " + value);
        }
    }

    /**
     * 경력 수준 Enum
     */
    public enum ExperienceLevel implements EnumValue {
        NEW_GRAD(0),       // 신입
        JUNIOR(1),         // 주니어 (1-3년)
        MID_LEVEL(2),      // 미드레벨 (3-5년)
        SENIOR(3);         // 시니어 (5년 이상)

        private final int value;

        ExperienceLevel(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static ExperienceLevel fromValue(int value) {
            for (ExperienceLevel level : ExperienceLevel.values()) {
                if (level.value == value) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Unknown experience level value: " + value);
        }
    }

    /**
     * 공고 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }

    /**
     * 마감 형식 컨버터
     */
    @Converter(autoApply = true)
    public static class CloseTypeConverter extends EnumValueConverter<CloseType> {
        @Override
        protected CloseType fromValue(Integer value) {
            return value == null ? null : CloseType.fromValue(value);
        }
    }

    /**
     * 경력 수준 컨버터
     */
    @Converter(autoApply = true)
    public static class ExperienceLevelConverter extends EnumValueConverter<ExperienceLevel> {
        @Override
        protected ExperienceLevel fromValue(Integer value) {
            return value == null ? null : ExperienceLevel.fromValue(value);
        }
    }
}