package com.highfive.meetu.domain.payment.admin.service;

import com.highfive.meetu.domain.payment.business.dto.PaymentBusinessDTO;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import com.highfive.meetu.domain.payment.common.repository.PaymentRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentAdminService {

    private final PaymentRepository paymentRepository;

    // 전체 결제 내역 조회 (페이징)
    public ResultData<Page<PaymentBusinessDTO>> getAllPayments(Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        Page<PaymentBusinessDTO> dtoPage = paymentPage.map(PaymentBusinessDTO::fromEntity);

        return ResultData.success(dtoPage.getNumberOfElements(), dtoPage);
    }


    // (선택) 특정 accountId로 기업별 결제 내역 조회도 확장 가능
    public ResultData<List<PaymentBusinessDTO>> getPaymentsByAccount(Long accountId) {
        List<Payment> payments = paymentRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId);
        List<PaymentBusinessDTO> dtos = payments.stream()
                .map(PaymentBusinessDTO::fromEntity)
                .toList();
        return ResultData.success(dtos.size(), dtos);
    }

    // 결제 상세
    public ResultData<PaymentBusinessDTO> getPaymentDetail(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new NotFoundException("결제 정보 없음"));
        return ResultData.success(1, PaymentBusinessDTO.fromEntity(payment));
    }
}
