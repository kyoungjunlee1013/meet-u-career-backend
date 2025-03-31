package com.highfive.meetu.domain.job.common.type;

import com.highfive.meetu.global.common.converter.EnumValueConverter;
import com.highfive.meetu.global.common.converter.EnumValueConverter.EnumValue;
import jakarta.persistence.Converter;

/**
 * 구직 관련 공통 Enum 및 컨버터 클래스 모음
 */
public class JobPostingTypes {

    /**
     * 연봉 코드 Enum
     * 사람인 API 기준 연봉 코드와 매칭
     */
    public enum SalaryCode implements EnumValue {
        COMPANY_RULE(0),         // 회사내규에 따름
        OVER_2600(9),            // 2,600만원 이상
        OVER_2800(10),           // 2,800만원 이상
        OVER_3000(11),           // 3,000만원 이상
        OVER_3200(12),           // 3,200만원 이상
        OVER_3400(13),           // 3,400만원 이상
        OVER_3600(14),           // 3,600만원 이상
        OVER_3800(15),           // 3,800만원 이상
        OVER_4000(16),           // 4,000만원 이상
        OVER_5000(17),           // 5,000만원 이상
        OVER_6000(18),           // 6,000만원 이상
        OVER_7000(19),           // 7,000만원 이상
        RANGE_8000_9000(20),     // 8,000~9,000만원
        RANGE_9000_10000(21),    // 9,000~1억원
        OVER_10000(22),          // 1억원 이상
        AFTER_INTERVIEW(99),     // 면접후 결정
        MONTHLY(101),            // 월급
        WEEKLY(102),             // 주급
        DAILY(103),              // 일급
        HOURLY(104),             // 시급
        PER_CASE(105);           // 건당

        private final int value;

        SalaryCode(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static SalaryCode fromValue(int value) {
            for (SalaryCode code : SalaryCode.values()) {
                if (code.value == value) {
                    return code;
                }
            }
            throw new IllegalArgumentException("Unknown salary code value: " + value);
        }

        public String getDescription() {
            return switch (this) {
                case COMPANY_RULE -> "회사내규에 따름";
                case OVER_2600 -> "2,600만원 이상";
                case OVER_2800 -> "2,800만원 이상";
                case OVER_3000 -> "3,000만원 이상";
                case OVER_3200 -> "3,200만원 이상";
                case OVER_3400 -> "3,400만원 이상";
                case OVER_3600 -> "3,600만원 이상";
                case OVER_3800 -> "3,800만원 이상";
                case OVER_4000 -> "4,000만원 이상";
                case OVER_5000 -> "5,000만원 이상";
                case OVER_6000 -> "6,000만원 이상";
                case OVER_7000 -> "7,000만원 이상";
                case RANGE_8000_9000 -> "8,000~9,000만원";
                case RANGE_9000_10000 -> "9,000~1억원";
                case OVER_10000 -> "1억원 이상";
                case AFTER_INTERVIEW -> "면접후 결정";
                case MONTHLY -> "월급";
                case WEEKLY -> "주급";
                case DAILY -> "일급";
                case HOURLY -> "시급";
                case PER_CASE -> "건당";
            };
        }
    }

    /**
     * 학력 코드 Enum
     * 사람인 API 기준 학력 코드와 매칭
     */
    public enum EducationLevel implements EnumValue {
        ANY(0),                  // 학력무관
        HIGH_SCHOOL(1),          // 고등학교졸업
        COLLEGE(2),              // 대학졸업(2,3년)
        UNIVERSITY(3),           // 대학교졸업(4년)
        MASTERS(4),              // 석사졸업
        DOCTORATE(5),            // 박사졸업
        HIGH_SCHOOL_ABOVE(6),    // 고등학교졸업이상
        COLLEGE_ABOVE(7),        // 대학졸업(2,3년)이상
        UNIVERSITY_ABOVE(8),     // 대학교졸업(4년)이상
        MASTERS_ABOVE(9);        // 석사졸업이상

        private final int value;

        EducationLevel(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static EducationLevel fromValue(int value) {
            for (EducationLevel level : EducationLevel.values()) {
                if (level.value == value) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Unknown education level value: " + value);
        }

        public String getDescription() {
            return switch (this) {
                case ANY -> "학력무관";
                case HIGH_SCHOOL -> "고등학교졸업";
                case COLLEGE -> "대학졸업(2,3년)";
                case UNIVERSITY -> "대학교졸업(4년)";
                case MASTERS -> "석사졸업";
                case DOCTORATE -> "박사졸업";
                case HIGH_SCHOOL_ABOVE -> "고등학교졸업이상";
                case COLLEGE_ABOVE -> "대학졸업(2,3년)이상";
                case UNIVERSITY_ABOVE -> "대학교졸업(4년)이상";
                case MASTERS_ABOVE -> "석사졸업이상";
            };
        }
    }

    /**
     * 경력 코드 Enum
     * 이미 JobPostingTypes에 정의된 ExperienceLevel을 참고하여 통합
     */
    public enum ExperienceLevel implements EnumValue {
        NEW_GRAD(0),    // 신입
        JUNIOR(1),      // 주니어 (1-3년)
        MID_LEVEL(2),   // 미드레벨 (4-7년)
        SENIOR(3);      // 시니어 (8년 이상)

        private final int value;

        ExperienceLevel(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static ExperienceLevel fromValue(int value) {
            for (ExperienceLevel level : ExperienceLevel.values()) {
                if (level.value == value) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Unknown experience level value: " + value);
        }

        public String getDescription() {
            return switch (this) {
                case NEW_GRAD -> "신입";
                case JUNIOR -> "경력 1-3년";
                case MID_LEVEL -> "경력 4-7년";
                case SENIOR -> "경력 8년 이상";
            };
        }
    }

    /**
     * 공고 상태 Enum
     */
    public enum Status implements EnumValue {
        INACTIVE(0),  // 비활성
        PENDING(1),   // 승인 대기
        ACTIVE(2);    // 활성

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status value: " + value);
        }

        public String getDescription() {
            return switch (this) {
                case INACTIVE -> "비활성";
                case PENDING -> "승인 대기";
                case ACTIVE -> "활성";
            };
        }
    }

    /**
     * 공고 마감 형식 Enum
     */
    public enum CloseType implements EnumValue {
        DEADLINE(1),     // 마감일 기준
        UPON_HIRING(2);  // 채용 시 마감

        private final int value;

        CloseType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static CloseType fromValue(int value) {
            for (CloseType type : CloseType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown close type value: " + value);
        }

        public String getDescription() {
            return switch (this) {
                case DEADLINE -> "마감일 기준";
                case UPON_HIRING -> "채용 시 마감";
            };
        }
    }

    /**
     * 연봉 코드 컨버터
     */
    @Converter(autoApply = true)
    public static class SalaryCodeConverter extends EnumValueConverter<SalaryCode> {
        @Override
        protected SalaryCode fromValue(Integer value) {
            return value == null ? null : SalaryCode.fromValue(value);
        }
    }

    /**
     * 학력 코드 컨버터
     */
    @Converter(autoApply = true)
    public static class EducationLevelConverter extends EnumValueConverter<EducationLevel> {
        @Override
        protected EducationLevel fromValue(Integer value) {
            return value == null ? null : EducationLevel.fromValue(value);
        }
    }

    /**
     * 경력 코드 컨버터
     */
    @Converter(autoApply = true)
    public static class ExperienceLevelConverter extends EnumValueConverter<ExperienceLevel> {
        @Override
        protected ExperienceLevel fromValue(Integer value) {
            return value == null ? null : ExperienceLevel.fromValue(value);
        }
    }

    /**
     * 공고 상태 컨버터
     */
    @Converter(autoApply = true)
    public static class StatusConverter extends EnumValueConverter<Status> {
        @Override
        protected Status fromValue(Integer value) {
            return value == null ? null : Status.fromValue(value);
        }
    }

    /**
     * 마감 형식 컨버터
     */
    @Converter(autoApply = true)
    public static class CloseTypeConverter extends EnumValueConverter<CloseType> {
        @Override
        protected CloseType fromValue(Integer value) {
            return value == null ? null : CloseType.fromValue(value);
        }
    }
}