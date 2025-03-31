package com.highfive.meetu.domain.resume.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 이력서 항목 관련 Enum 및 컨버터 클래스 모음
 */
public class ResumeContentTypes {

    /**
     * 이력서 항목 유형 Enum
     */
    public enum SectionType implements EnumValue {
        EDUCATION(0),   // 학력
        EXPERIENCE(1),  // 경력
        CERTIFICATE(2), // 자격증
        ACTIVITY(3);    // 활동

        private final int value;

        SectionType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static SectionType fromValue(int value) {
            for (SectionType type : SectionType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown section type value: " + value);
        }
    }

    /**
     * 이력서 항목 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class SectionTypeConverter extends EnumValueConverter<SectionType> {
        @Override
        protected SectionType fromValue(Integer value) {
            return value == null ? null : SectionType.fromValue(value);
        }
    }
}