package com.highfive.meetu.domain.application.common.entity;

import com.highfive.meetu.domain.application.common.type.InterviewReviewTypes.CareerLevel;
import com.highfive.meetu.domain.application.common.type.InterviewReviewTypes.Result;
import com.highfive.meetu.domain.application.common.type.InterviewReviewTypes.Status;
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
    private CareerLevel careerLevel;  // 면접 당시 경력 (0: 신입, 1: 경력)

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
    private Result result;  // 면접 결과 (0: 불합격, 1: 합격, 2: 대기중)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 리뷰 작성일

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 리뷰 수정일

    @Column(nullable = false)
    private Status status;  // 리뷰 상태 (0: 활성, 1: 삭제 요청)

    /**
     * 리뷰 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 리뷰가 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    /**
     * 특정 면접 유형이 선택되었는지 확인
     */
    public boolean hasInterviewType(int typeFlag) {
        return (this.interviewType & typeFlag) == typeFlag;
    }

    /**
     * 면접 유형 추가
     */
    public void addInterviewType(int typeFlag) {
        this.interviewType |= typeFlag;
    }

    /**
     * 면접 유형 제거
     */
    public void removeInterviewType(int typeFlag) {
        this.interviewType &= ~typeFlag;
    }
}