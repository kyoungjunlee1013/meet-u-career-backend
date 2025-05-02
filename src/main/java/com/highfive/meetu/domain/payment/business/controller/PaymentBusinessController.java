package com.highfive.meetu.domain.payment.business.controller;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.business.service.JobBusinessService;
import com.highfive.meetu.domain.payment.business.dto.PaymentBusinessDTO;
import com.highfive.meetu.domain.payment.business.dto.TossPaymentConfirmRequest;
import com.highfive.meetu.domain.payment.business.service.PaymentBusinessService;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business/payment")
public class PaymentBusinessController {

    private final PaymentBusinessService paymentBusinessService;

//    // Toss 결제 승인 요청
//    @PostMapping("/confirm")
//    public ResultData<String> confirmPayment(@RequestBody TossPaymentConfirmRequest request) {
//        String result = paymentBusinessService.confirmPayment(request);
//        return ResultData.success(1, result);
//    }

    // 기업회원 결제 목록
    @GetMapping("/history")
    public ResultData<List<PaymentBusinessDTO>> getMyPaymentHistory() {
        //Long accountId = SecurityUtil.getAccountId();
        Long accountId = 1L;
        return paymentBusinessService.getPaymentHistoryByAccount(accountId);
    }
    
    // 기업회원 결제 상세
    @GetMapping("/{transactionId}")
    public ResultData<PaymentBusinessDTO> getPaymentDetailByTransactionId(
            @PathVariable String transactionId) {
        return paymentBusinessService.getPaymentDetailByTransactionId(transactionId);
    }



}
