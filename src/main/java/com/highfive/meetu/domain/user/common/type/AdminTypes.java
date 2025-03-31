package com.highfive.meetu.domain.user.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 관리자 관련 Enum 및 컨버터 클래스 모음
 */
public class AdminTypes {

    /**
     * 관리자 역할 Enum
     */
    public enum Role implements EnumValue {
        SUPER(1),   // 슈퍼 관리자
        ADMIN(2);   // 일반 관리자

        private final int value;

        Role(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Role fromValue(int value) {
            for (Role role : Role.values()) {
                if (role.getValue() == value) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Unknown admin role value: " + value);
        }
    }

    /**
     * 관리자 역할 컨버터
     */
    @Converter(autoApply = true)
    public static class RoleConverter extends EnumValueConverter<Role> {
        @Override
        protected Role fromValue(Integer value) {
            return value == null ? null : Role.fromValue(value);
        }
    }
}