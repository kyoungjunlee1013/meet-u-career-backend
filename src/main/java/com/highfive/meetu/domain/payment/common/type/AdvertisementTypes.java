package com.highfive.meetu.domain.payment.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 광고 관련 Enum 및 컨버터 클래스 모음
 */
public class AdvertisementTypes {

    /**
     * 광고 유형 Enum
     */
    public enum AdType implements EnumValue {
        BASIC(1),      // 기본 광고
        STANDARD(2),   // 일반 광고
        PREMIUM(3);    // 프리미엄 광고

        private final int value;

        AdType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static AdType fromValue(int value) {
            for (AdType type : AdType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown ad type value: " + value);
        }
    }

    /**
     * 광고 상태 Enum
     */
    public enum AdStatus implements EnumValue {
        ACTIVE(1),           // 활성 상태
        PAUSED(2),           // 일시 중지
        EXPIRED(3),          // 만료됨
        PENDING_APPROVAL(4); // 승인 대기 중

        private final int value;

        AdStatus(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static AdStatus fromValue(int value) {
            for (AdStatus status : AdStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown ad status value: " + value);
        }
    }

    /**
     * 광고 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class AdTypeConverter extends EnumValueConverter<AdType> {
        @Override
        protected AdType fromValue(Integer value) {
            return value == null ? null : AdType.fromValue(value);
        }
    }

    /**
     * 광고 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class AdStatusConverter extends EnumValueConverter<AdStatus> {
        @Override
        protected AdStatus fromValue(Integer value) {
            return value == null ? null : AdStatus.fromValue(value);
        }
    }
}