package com.highfive.meetu.domain.payment.business.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementRegisterRequest {
    private Long jobPostingId;
    private Integer adType;         // 광고 유형 (BASIC, STANDARD, PREMIUM)
    private Integer durationDays;   // 광고 기간
    private String paymentKey;      // Toss 결제 키
    private String orderId;
    private Long amount;
}

