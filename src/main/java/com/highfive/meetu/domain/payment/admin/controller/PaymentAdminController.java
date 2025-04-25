package com.highfive.meetu.domain.payment.admin.controller;

import com.highfive.meetu.domain.payment.admin.service.PaymentAdminService;
import com.highfive.meetu.domain.payment.business.dto.PaymentBusinessDTO;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payment")
public class PaymentAdminController {

    private final PaymentAdminService paymentAdminService;

    // 전체 결제 내역 조회 (관리자)
    @GetMapping("/history")
    public ResultData<Page<PaymentBusinessDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return paymentAdminService.getAllPayments(PageRequest.of(page, size));
    }


    // 결제 상세 조회 (관리자)
    @GetMapping("/{transactionId}")
    public ResultData<PaymentBusinessDTO> getPaymentDetail(@PathVariable String transactionId) {
        return paymentAdminService.getPaymentDetail(transactionId);
    }
}


