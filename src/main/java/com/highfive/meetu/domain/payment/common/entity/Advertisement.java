package com.highfive.meetu.domain.payment.common.entity;

import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.payment.common.type.AdvertisementTypes.AdType;
import com.highfive.meetu.domain.payment.common.type.AdvertisementTypes.AdStatus;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 광고 엔티티
 *
 * 연관관계:
 * - JobPosting(1) : Advertisement(N) - Advertisement가 주인, @JoinColumn 사용
 * - Company(1) : Advertisement(N) - Advertisement가 주인, @JoinColumn 사용
 * - Payment(1) : Advertisement(N) - Advertisement가 주인, @JoinColumn 사용
 */
@Entity(name = "advertisement")
@Table(
        indexes = {
                @Index(name = "idx_ad_jobPostingId", columnList = "jobPostingId"),
                @Index(name = "idx_ad_companyId", columnList = "companyId"),
                @Index(name = "idx_ad_startDate", columnList = "startDate"),
                @Index(name = "idx_ad_endDate", columnList = "endDate"),
                @Index(name = "idx_ad_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"jobPosting", "company", "payment"})
public class Advertisement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPostingId", nullable = false)
    private JobPosting jobPosting;  // 광고 대상 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 광고를 신청한 기업

    @Column(nullable = false)
    private Integer adType;  // 광고 유형 (1: BASIC, 2: STANDARD, 3: PREMIUM)

    @Column(nullable = false)
    private Integer status;  // 광고 상태 (1: 활성, 2: 일시중지, 3: 만료, 4: 승인대기)

    @Column(nullable = false)
    private Integer durationDays;  // 광고 기간 (단위: 일)

    @Column(nullable = false)
    private LocalDateTime startDate;  // 광고 시작 날짜

    @Column(nullable = false)
    private LocalDateTime endDate;  // 광고 종료 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId")
    private Payment payment;  // 광고 결제 (NULL 가능)

    /**
     * 광고가 현재 활성 상태인지 확인
     */
    public boolean isActive() {
        // 상태가 활성(1)이고, 현재 시간이 시작일과 종료일 사이에 있는지 확인
        LocalDateTime now = LocalDateTime.now();
        return status == AdStatus.ACTIVE.getValue() &&
                now.isAfter(startDate) &&
                now.isBefore(endDate);
    }

    /**
     * 광고가 만료되었는지 확인
     */
    public boolean isExpired() {
        return status == AdStatus.EXPIRED.getValue() ||
                LocalDateTime.now().isAfter(endDate);
    }

    /**
     * 광고가 일시중지 상태인지 확인
     */
    public boolean isPaused() {
        return status == AdStatus.PAUSED.getValue();
    }

    /**
     * 광고가 승인 대기 중인지 확인
     */
    public boolean isPendingApproval() {
        return status == AdStatus.PENDING_APPROVAL.getValue();
    }

    /**
     * 광고 시작까지 남은 시간 계산 (단위: 시간)
     */
    public long hoursUntilStart() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(startDate)) {
            return 0;
        }
        return java.time.Duration.between(now, startDate).toHours();
    }

    /**
     * 광고 종료까지 남은 시간 계산 (단위: 시간)
     */
    public long hoursUntilEnd() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)) {
            return 0;
        }
        return java.time.Duration.between(now, endDate).toHours();
    }
}