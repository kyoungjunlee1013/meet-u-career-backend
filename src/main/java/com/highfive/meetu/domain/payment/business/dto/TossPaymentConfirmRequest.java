package com.highfive.meetu.domain.payment.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentConfirmRequest {
    private String paymentKey; // Toss에서 전달받은 결제 키
    private String orderId;    // 주문 ID
    private Long amount;       // 결제 금액
}

