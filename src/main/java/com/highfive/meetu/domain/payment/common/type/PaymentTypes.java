package com.highfive.meetu.domain.payment.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 결제 관련 Enum 및 컨버터 클래스 모음
 */
public class PaymentTypes {

    /**
     * 결제 상태 Enum
     */
    public enum Status implements EnumValue {
        FAILED(0),  // 실패
        SUCCESS(1); // 성공

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
            throw new IllegalArgumentException("Unknown payment status value: " + value);
        }
    }

    /**
     * 결제 제공업체 Enum
     */
    public enum Provider implements EnumValue {
        TOSS("TOSS"),
        KAKAO("KAKAO");

        private final String value;

        Provider(String value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return this.ordinal();
        }

        public String getStringValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Provider fromValue(int value) {
            for (Provider provider : Provider.values()) {
                if (provider.ordinal() == value) {
                    return provider;
                }
            }
            throw new IllegalArgumentException("Unknown payment provider value: " + value);
        }

        /**
         * 문자열로부터 Enum 인스턴스 찾기
         */
        public static Provider fromString(String value) {
            for (Provider provider : Provider.values()) {
                if (provider.value.equals(value)) {
                    return provider;
                }
            }
            throw new IllegalArgumentException("Unknown payment provider: " + value);
        }
    }

    /**
     * 결제 방식 Enum
     */
    public enum Method implements EnumValue {
        CARD("CARD"),
        BANK_TRANSFER("BANK_TRANSFER");

        private final String value;

        Method(String value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return this.ordinal();
        }

        public String getStringValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Method fromValue(int value) {
            for (Method method : Method.values()) {
                if (method.ordinal() == value) {
                    return method;
                }
            }
            throw new IllegalArgumentException("Unknown payment method value: " + value);
        }

        /**
         * 문자열로부터 Enum 인스턴스 찾기
         */
        public static Method fromString(String value) {
            for (Method method : Method.values()) {
                if (method.value.equals(value)) {
                    return method;
                }
            }
            throw new IllegalArgumentException("Unknown payment method: " + value);
        }
    }

    /**
     * 결제 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }

    /**
     * 결제 제공업체 컨버터
     */
    @Converter(autoApply = true)
    public static class ProviderConverter extends EnumValueConverter<Provider> {
        @Override
        protected Provider fromValue(Integer value) {
            return value == null ? null : Provider.fromValue(value);
        }
    }

    /**
     * 결제 방식 컨버터
     */
    @Converter(autoApply = true)
    public static class MethodConverter extends EnumValueConverter<Method> {
        @Override
        protected Method fromValue(Integer value) {
            return value == null ? null : Method.fromValue(value);
        }
    }
}