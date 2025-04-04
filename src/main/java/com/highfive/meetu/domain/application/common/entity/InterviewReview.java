package com.highfive.meetu.domain.application.common.entity;

import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 면접 리뷰 엔티티
 *
 * 연관관계:
 * - Profile(1) : InterviewReview(N) - InterviewReview가 주인, @JoinColumn 사용
 * - Company(1) : InterviewReview(N) - InterviewReview가 주인, @JoinColumn 사용
 * - Application(1) : InterviewReview(1) - InterviewReview가 주인, @JoinColumn 사용
 * - JobCategory(1) : InterviewReview(N) - InterviewReview가 주인, @JoinColumn 사용
 */
@Entity(name = "interviewReview")
@Table(
        indexes = {
                @Index(name = "idx_interview_profileId", columnList = "profileId"),
                @Index(name = "idx_interview_companyId", columnList = "companyId"),
                @Index(name = "idx_interview_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"profile", "company", "application", "jobCategory"})
public class InterviewReview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;  // 리뷰 작성자의 프로필

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 면접 본 기업

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationId", nullable = false, unique = true)
    private Application application;  // 리뷰가 연결된 지원 내역

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobCategoryId", nullable = false)
    private JobCategory jobCategory;  // 직무 카테고리

    @Column(nullable = false)
    private Integer careerLevel;  // 면접 당시 경력 (0: 신입, 1: 경력)

    @Column(length = 7, nullable = false)
    private String interviewYearMonth;  // 면접 연도와 월 (YYYY-MM 형식)

    @Column(nullable = false)
    private Integer rating;  // 전반적인 평가 (0: 부정적, 1: 보통, 2: 긍정적)

    @Column(nullable = false)
    private Integer difficulty;  // 난이도 (1~5점)

    @Column(nullable = false)
    private Integer interviewType;  // 면접 유형 비트맵

    @Column(nullable = false)
    private Integer interviewParticipants;  // 면접 인원 (0: 1:1면접, 1: 지원자 1명, 면접관 다수, 2: 그룹면접)

    @Column(nullable = false)
    private Boolean hasFrequentQuestions;  // 자주 나오는 질문에서 선택하기

    @Column(columnDefinition = "TEXT")
    private String questionsAsked;  // 면접 질문

    @Column(columnDefinition = "TEXT")
    private String interviewTip;  // 면접 팁

    @Column(nullable = false)
    private Integer result;  // 면접 결과 (0: 불합격, 1: 합격, 2: 대기중)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 리뷰 수정일

    @Column(nullable = false)
    private Integer status;  // 리뷰 상태 (0: 활성, 1: 삭제 요청)

    // 상태 상수 정의
    public static class Status {
        public static final int ACTIVE = 0;
        public static final int DELETE_REQUESTED = 1;
    }

    // 선택값 상수 정의 (enum 대신)
    public static class CareerLevel {
        public static final int NEW_GRAD = 0;
        public static final int EXPERIENCED = 1;
    }

    public static class Rating {
        public static final int NEGATIVE = 0;
        public static final int NEUTRAL = 1;
        public static final int POSITIVE = 2;
    }

    public static class InterviewType {
        public static final int JOB_PERSONALITY = 1;
        public static final int DISCUSSION = 2;
        public static final int APTITUDE_TEST = 4;
        public static final int PRESENTATION = 8;
        public static final int PRACTICAL_TEST = 16;
        public static final int OTHER = 32;
    }

    public static class InterviewParticipants {
        public static final int ONE_ON_ONE = 0;
        public static final int ONE_TO_MANY = 1;
        public static final int GROUP = 2;
    }

    public static class Result {
        public static final int FAILED = 0;
        public static final int PASSED = 1;
        public static final int PENDING = 2;
    }
}