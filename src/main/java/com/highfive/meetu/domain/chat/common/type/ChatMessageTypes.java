package com.highfive.meetu.domain.chat.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 채팅 메시지 관련 Enum 및 컨버터 클래스 모음
 */
public class ChatMessageTypes {

    /**
     * 발신자 유형 Enum
     */
    public enum SenderType implements EnumValue {
        PERSONAL(0),  // 개인 계정
        BUSINESS(1);  // 기업 계정

        private final int value;

        SenderType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static SenderType fromValue(int value) {
            for (SenderType type : SenderType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown sender type value: " + value);
        }
    }

    /**
     * 읽음 상태 Enum
     */
    public enum ReadStatus implements EnumValue {
        UNREAD(0),  // 안 읽음
        READ(1);    // 읽음

        private final int value;

        ReadStatus(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static ReadStatus fromValue(int value) {
            for (ReadStatus status : ReadStatus.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown read status value: " + value);
        }
    }

    /**
     * 발신자 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class SenderTypeConverter extends EnumValueConverter<SenderType> {
        @Override
        protected SenderType fromValue(Integer value) {
            return value == null ? null : SenderType.fromValue(value);
        }
    }

    /**
     * 읽음 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class ReadStatusConverter extends EnumValueConverter<ReadStatus> {
        @Override
        protected ReadStatus fromValue(Integer value) {
            return value == null ? null : ReadStatus.fromValue(value);
        }
    }
}