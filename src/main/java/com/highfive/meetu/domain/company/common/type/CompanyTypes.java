package com.highfive.meetu.domain.company.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 기업 관련 Enum 및 컨버터 클래스 모음
 * - 한 파일에 관련 Enum과 컨버터를 정의하여 파일 수 최소화
 * - 관련 Enum 타입을 그룹화하여 관리
 */
public class CompanyTypes {

    /**
     * 기업 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),    // 활성
        INACTIVE(1);  // 비활성

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
            throw new IllegalArgumentException("Unknown company status value: " + value);
        }
    }

    /**
     * 기업 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }

    /**
     * 필요한 경우 기업 산업 분야나 규모 등의 추가 enum 타입 정의 가능
     */
}