package com.highfive.meetu.domain.payment.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.payment.common.type.PaymentTypes.Method;
import com.highfive.meetu.domain.payment.common.type.PaymentTypes.Provider;
import com.highfive.meetu.domain.payment.common.type.PaymentTypes.Status;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 결제 엔티티
 *
 * 연관관계:
 * - Account(1) : Payment(N) - Payment가 주인, @JoinColumn 사용
 * - Payment(1) : Advertisement(N) - Payment가 비주인, mappedBy 사용
 */
@Entity(name = "payment")
@Table(
        indexes = {
                @Index(name = "idx_payment_accountId", columnList = "accountId"),
                @Index(name = "idx_payment_status", columnList = "status"),
                @Index(name = "idx_payment_createdAt", columnList = "createdAt")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "advertisements"})
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 결제한 계정 (기업 회원)

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;  // 결제 금액

    @Column(nullable = false)
    private Status status;  // 결제 상태 (FAILED, SUCCESS) - 컨버터 자동 적용

    @Column(length = 20, nullable = false)
    private Provider provider;  // 결제 제공업체 (TOSS, KAKAO) - 컨버터 자동 적용

    @Column(length = 50, nullable = false)
    private Method method;  // 결제 방식 (CARD, BANK_TRANSFER) - 컨버터 자동 적용

    @Column(length = 100, nullable = false, unique = true)
    private String transactionId;  // 거래 ID (중복 방지)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 결제 상태 변경일

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"payment"})
    @Builder.Default
    private List<Advertisement> advertisements = new ArrayList<>();

    /**
     * 결제 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 결제가 성공했는지 확인
     */
    public boolean isSuccessful() {
        return this.status == Status.SUCCESS;
    }

    /**
     * 광고 추가 편의 메서드
     */
    public void addAdvertisement(Advertisement advertisement) {
        this.advertisements.add(advertisement);
        advertisement.setPayment(this);
    }
}