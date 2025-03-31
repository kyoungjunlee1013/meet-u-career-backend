package com.highfive.meetu.domain.application.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 면접 리뷰 관련 Enum 및 컨버터 클래스 모음
 */
public class InterviewReviewTypes {

    /**
     * 지원자 경력 수준 Enum
     */
    public enum CareerLevel implements EnumValue {
        ROOKIE(0),   // 신입
        CAREER(1);   // 경력

        private final int value;

        CareerLevel(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static CareerLevel fromValue(int value) {
            for (CareerLevel level : CareerLevel.values()) {
                if (level.value == value) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Unknown career level value: " + value);
        }
    }

    /**
     * 면접 평가 Enum
     */
    public enum Rating implements EnumValue {
        NEGATIVE(0),  // 부정적
        NEUTRAL(1),   // 보통
        POSITIVE(2);  // 긍정적

        private final int value;

        Rating(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Rating fromValue(int value) {
            for (Rating rating : Rating.values()) {
                if (rating.value == value) {
                    return rating;
                }
            }
            throw new IllegalArgumentException("Unknown rating value: " + value);
        }
    }

    /**
     * 면접 인원 유형 Enum
     */
    public enum InterviewParticipants implements EnumValue {
        ONE_ON_ONE(0),           // 1:1 면접
        ONE_TO_MANY(1),          // 지원자 1명, 면접관 다수
        GROUP_INTERVIEW(2);      // 그룹면접

        private final int value;

        InterviewParticipants(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static InterviewParticipants fromValue(int value) {
            for (InterviewParticipants participants : InterviewParticipants.values()) {
                if (participants.value == value) {
                    return participants;
                }
            }
            throw new IllegalArgumentException("Unknown interview participants value: " + value);
        }
    }

    /**
     * 면접 결과 Enum
     */
    public enum Result implements EnumValue {
        REJECTED(0),   // 불합격
        ACCEPTED(1),   // 합격
        PENDING(2);    // 대기중

        private final int value;

        Result(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        /**
         * 정수값으로부터 Enum 인스턴스 찾기
         */
        public static Result fromValue(int value) {
            for (Result result : Result.values()) {
                if (result.value == value) {
                    return result;
                }
            }
            throw new IllegalArgumentException("Unknown interview result value: " + value);
        }
    }

    /**
     * 리뷰 상태 Enum
     */
    public enum Status implements EnumValue {
        ACTIVE(0),    // 활성
        DELETED(1);   // 삭제 요청

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
            throw new IllegalArgumentException("Unknown review status value: " + value);
        }
    }

    /**
     * 지원자 경력 수준 컨버터
     */
    @Converter(autoApply = true)
    public static class CareerLevelConverter extends EnumValueConverter<CareerLevel> {
        @Override
        protected CareerLevel fromValue(Integer value) {
            return value == null ? null : CareerLevel.fromValue(value);
        }
    }

    /**
     * 면접 평가 컨버터
     */
    @Converter(autoApply = true)
    public static class RatingConverter extends EnumValueConverter<Rating> {
        @Override
        protected Rating fromValue(Integer value) {
            return value == null ? null : Rating.fromValue(value);
        }
    }

    /**
     * 면접 인원 유형 컨버터
     */
    @Converter(autoApply = true)
    public static class InterviewParticipantsConverter extends EnumValueConverter<InterviewParticipants> {
        @Override
        protected InterviewParticipants fromValue(Integer value) {
            return value == null ? null : InterviewParticipants.fromValue(value);
        }
    }

    /**
     * 면접 결과 컨버터
     */
    @Converter(autoApply = true)
    public static class ResultConverter extends EnumValueConverter<Result> {
        @Override
        protected Result fromValue(Integer value) {
            return value == null ? null : Result.fromValue(value);
        }
    }

    /**
     * 리뷰 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }
}