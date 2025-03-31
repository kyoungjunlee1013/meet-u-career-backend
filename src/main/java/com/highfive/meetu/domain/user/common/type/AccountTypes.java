package com.highfive.meetu.domain.user.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 계정 관련 Enum 및 컨버터 클래스 모음
 */
public class AccountTypes {

    /**
     * 계정 유형 Enum
     */
    public enum Type implements EnumValue {
        PERSONAL(0),  // 개인 계정
        BUSINESS(1);  // 기업 계정

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
            throw new IllegalArgumentException("Unknown account type value: " + value);
        }
    }

    /**
     * OAuth 제공자 Enum
     */
    public enum OAuthProvider implements EnumValue {
        GOOGLE(1),    // Google 로그인
        KAKAO(2),     // Kakao 로그인
        NAVER(3);     // Naver 로그인

        private final int value;

        OAuthProvider(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static OAuthProvider fromValue(int value) {
            for (OAuthProvider provider : OAuthProvider.values()) {
                if (provider.value == value) {
                    return provider;
                }
            }
            throw new IllegalArgumentException("Unknown OAuth provider value: " + value);
        }
    }

    /**
     * 계정 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),            // 활성
        INACTIVE(1),          // 비활성
        PENDING_APPROVAL(2),  // 기업계정 승인 대기 중
        REJECTED(3);          // 기업계정 반려됨

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
            throw new IllegalArgumentException("Unknown account status value: " + value);
        }
    }

    /**
     * 계정 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class TypeConverter extends EnumValueConverter<Type> {
        @Override
        protected Type fromValue(Integer value) {
            return value == null ? null : Type.fromValue(value);
        }
    }

    /**
     * OAuth 제공자 컨버터
     */
    @Converter(autoApply = true)
    public static class OAuthProviderConverter extends EnumValueConverter<OAuthProvider> {
        @Override
        protected OAuthProvider fromValue(Integer value) {
            return value == null ? null : OAuthProvider.fromValue(value);
        }
    }

    /**
     * 계정 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}