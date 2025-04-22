package com.highfive.meetu.domain.payment.business.dto;

import com.highfive.meetu.domain.payment.common.entity.Payment;
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

    public static PaymentBusinessDTO fromEntity(Payment payment) {
        return PaymentBusinessDTO.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .provider(payment.getProvider())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}

