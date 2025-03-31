package com.highfive.meetu.domain.community.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 커뮤니티 댓글 관련 Enum 및 컨버터 클래스 모음
 */
public class CommunityCommentTypes {

    /**
     * 댓글 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),   // 활성
        DELETED(1);  // 삭제됨

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
            throw new IllegalArgumentException("Unknown comment status value: " + value);
        }
    }

    /**
     * 댓글 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}