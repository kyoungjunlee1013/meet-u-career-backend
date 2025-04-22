package com.highfive.meetu.domain.payment.business.service;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.payment.business.dto.PaymentBusinessDTO;
import com.highfive.meetu.domain.payment.business.dto.TossPaymentConfirmRequest;
import com.highfive.meetu.domain.payment.business.dto.TossPaymentResponse;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import com.highfive.meetu.domain.payment.common.repository.PaymentRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentBusinessService {

    @Value("${api.toss.secretKey}")
    private String secretKey;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public String confirmPayment(TossPaymentConfirmRequest request) {
        String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String toEncode = secretKey + ":";
        System.out.println("🔐 원본: " + toEncode);
        System.out.println("🔐 인코딩: " + Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8)));

        System.out.println(secretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        Map<String, Object> body = new HashMap<>();
        body.put("orderId", request.getOrderId());
        body.put("amount", request.getAmount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TossPaymentResponse> response = new RestTemplate().exchange(
                    "https://api.tosspayments.com/v1/payments/" + request.getPaymentKey(),
                    HttpMethod.POST,
                    entity,
                    TossPaymentResponse.class
            );

            TossPaymentResponse toss = response.getBody();

            // 임시 account → 추후 SecurityUtil.getAccountId() 로 변경
            Account account = accountRepository.findById(1L)
                    .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

            Payment payment = Payment.builder()
                    .account(account)
                    .amount(BigDecimal.valueOf(toss.getAmount()))
                    .status(Payment.Status.SUCCESS)
                    .provider(Payment.Provider.TOSS)
                    .method(Payment.Method.CARD)  // 우선 CARD 고정
                    .transactionId(toss.getPaymentKey())
                    .updatedAt(LocalDateTime.parse(toss.getApprovedAt()))
                    .build();

            paymentRepository.save(payment);

            return toss.getPaymentKey();

        } catch (HttpClientErrorException e) {
            throw new BadRequestException("결제 실패: " + e.getResponseBodyAsString());
        }
    }

    // 기업회원 결제 목록 확인
    public ResultData<List<PaymentBusinessDTO>> getPaymentHistoryByAccount(Long accountId) {
        List<Payment> payments = paymentRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId);

        List<PaymentBusinessDTO> dtos = payments.stream()
                .map(PaymentBusinessDTO::fromEntity)
                .toList();

        return ResultData.success(dtos.size(), dtos);
    }

    // 기업회원 결제 상세
    public ResultData<PaymentBusinessDTO> getPaymentDetailByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new NotFoundException("결제 정보를 찾을 수 없습니다."));

        PaymentBusinessDTO dto = PaymentBusinessDTO.fromEntity(payment);
        return ResultData.success(1, dto);
    }


}

