package com.highfive.meetu.domain.resume.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 이력서 관련 Enum 및 컨버터 클래스 모음
 * - 한 파일에 관련 Enum과 컨버터를 정의하여 파일 수 최소화
 * - 관련 Enum 타입을 그룹화하여 관리
 */
public class ResumeTypes {

    /**
     * 이력서 유형 Enum
     */
    public enum Type implements EnumValue {
        CUSTOM(0),  // 직접 작성
        FILE(1),    // 파일 첨부
        URL(2);     // URL 링크

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Type fromValue(int value) {
            for (Type type : Type.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown resume type value: " + value);
        }
    }

    /**
     * 이력서 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),  // 활성 상태
        DRAFT(1),   // 임시저장 상태
        DELETED(2); // 삭제 상태

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown resume status value: " + value);
        }
    }

    /**
     * 이력서 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class TypeConverter extends EnumValueConverter<Type> {
        @Override
        protected Type fromValue(Integer value) {
            return value == null ? null : Type.fromValue(value);
        }
    }

    /**
     * 이력서 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}