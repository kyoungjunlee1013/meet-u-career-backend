package com.highfive.meetu.domain.calendar.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 캘린더 이벤트 관련 Enum 및 컨버터 클래스 모음
 */
public class CalendarEventTypes {

    /**
     * 이벤트 유형 Enum
     * 1: 면접, 2: 지원마감, 3: 북마크 마감, 4: 기업 이벤트, 5: 개인 일정
     */
    public enum EventType implements EnumValue {
        INTERVIEW(1),                  // 면접
        APPLICATION_DEADLINE(2),       // 지원 마감일
        BOOKMARK_DEADLINE(3),          // 북마크한 공고 마감일
        COMPANY_EVENT(4),              // 기업 이벤트
        PERSONAL(5);                   // 개인 일정

        private final int value;

        EventType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return this.value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static EventType fromValue(int value) {
            for (EventType type : EventType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown event type value: " + value);
        }
    }

    /**
     * 이벤트 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class EventTypeConverter extends EnumValueConverter<EventType> {
        @Override
        protected EventType fromValue(Integer value) {
            return value == null ? null : EventType.fromValue(value);
        }
    }
}