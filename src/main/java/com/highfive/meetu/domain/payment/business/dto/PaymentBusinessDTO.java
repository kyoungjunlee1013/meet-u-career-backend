package com.highfive.meetu.domain.payment.business.dto;

import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentBusinessDTO {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private Integer provider;
    private Integer method;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer adType;  // 광고 유형 (1: BASIC, 2: STANDARD, 3: PREMIUM)

    // 광고/공고/기업 정보 추가
    private Long advertisementId;
    private String advertisementTitle;
    private Integer advertisementPeriod;
    private Integer advertisementStatus;
    private LocalDateTime advertisementStartDate;
    private LocalDateTime advertisementEndDate;
    private Long jobPostingId;
    private String companyName;

    public static PaymentBusinessDTO fromEntity(Payment payment) {
        // 광고 정보는 payment.getAdvertisements() 또는 payment.getAdvertisement() 등 연관관계에 따라 추출
        Advertisement ad = payment.getAdvertisement(); // 1:1 관계일 때 예시
        return PaymentBusinessDTO.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .provider(payment.getProvider())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .advertisementId(ad != null ? ad.getId() : null)
                .advertisementTitle(ad != null && ad.getJobPosting() != null ? ad.getJobPosting().getTitle() : null)
                .advertisementPeriod(ad != null ? ad.getDurationDays() : null)
                .advertisementStatus(ad != null ? ad.getStatus() : null)
                .advertisementStartDate(ad != null ? ad.getStartDate() : null)
                .advertisementEndDate(ad != null ? ad.getEndDate() : null)
                .jobPostingId(ad != null && ad.getJobPosting() != null ? ad.getJobPosting().getId() : null)
                .adType(ad != null ? ad.getAdType() : null)
                .companyName(ad != null && ad.getCompany() != null ? ad.getCompany().getName() : null)
                .build();
    }
}

