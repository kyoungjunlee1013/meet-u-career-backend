package com.highfive.meetu.domain.cs.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 고객센터 문의 관련 Enum 및 컨버터 클래스 모음
 */
public class CustomerSupportTypes {

    /**
     * 문의 카테고리 Enum
     */
    public enum Category implements EnumValue {
        PAYMENT(0),             // 결제 문의
        SERVICE(1),             // 서비스 이용 문의
        ACCOUNT(2),             // 계정 문의
        JOB_POSTING(3),         // 채용공고 문의
        COMPANY(4),             // 기업 문의
        RESUME(5),              // 이력서 문의
        APPLICATION(6),         // 지원 문의
        OTHER(7);               // 기타 문의

        private final int value;

        Category(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Category fromValue(int value) {
            for (Category category : Category.values()) {
                if (category.value == value) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Unknown category value: " + value);
        }

        /**
         * 문자열로부터 Enum 인스턴스 찾기
         */
        public static Category fromString(String value) {
            try {
                return Category.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown category: " + value);
            }
        }
    }

    /**
     * 문의 상태 Enum
     */
    public enum Status implements EnumValue {
        PENDING(0),       // 미처리
        IN_PROGRESS(1),   // 처리 중
        RESOLVED(2);      // 처리 완료

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
            throw new IllegalArgumentException("Unknown status value: " + value);
        }
    }

    /**
     * 문의 카테고리 컨버터
     */
    @Converter(autoApply = true)
    public static class CategoryConverter extends EnumValueConverter<Category> {
        @Override
        protected Category fromValue(Integer value) {
            return value == null ? null : Category.fromValue(value);
        }
    }

    /**
     * 문의 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}