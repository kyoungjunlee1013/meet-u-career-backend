package com.highfive.meetu.domain.resume.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 이력서 열람 로그 관련 Enum 및 컨버터 클래스 모음
 */
public class ResumeViewLogTypes {

    /**
     * 이력서 열람 유형 Enum
     */
    public enum ViewType implements EnumValue {
        GENERAL(0),      // 일반 열람
        EVALUATION(1);   // 평가 목적으로 열람

        private final int value;

        ViewType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static ViewType fromValue(int value) {
            for (ViewType type : ViewType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown resume view type value: " + value);
        }
    }

    /**
     * 이력서 열람 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class ViewTypeConverter extends EnumValueConverter<ViewType> {
        @Override
        protected ViewType fromValue(Integer value) {
            return value == null ? null : ViewType.fromValue(value);
        }
    }
}