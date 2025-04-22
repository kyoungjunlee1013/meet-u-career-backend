package com.highfive.meetu.domain.payment.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentResponse {
    private String paymentKey;   // 거래 ID (→ transactionId)
    private String orderId;
    private Long amount;
    private String method;
    private String approvedAt;
    private CardInfo card;

    @Getter @Setter
    public static class CardInfo {
        private String company;
    }
}

