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
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentBusinessService {

    @Value("${api.toss.secretKey}")
    private String secretKey;

    @Value("${api.toss.uri}")
    private String uri;

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    // 결제 승인
    public String confirmPayment(TossPaymentConfirmRequest request) {
        System.out.println("결제 요청 시작: " + request.getOrderId() + ", paymentKey: " + request.getPaymentKey());

        String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        Map<String, Object> body = new HashMap<>();
        body.put("orderId", request.getOrderId());
        body.put("amount", request.getAmount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // API 요청 전 로그 추가
            System.out.println("토스 API 요청: " + uri + request.getPaymentKey());
            System.out.println("요청 Body: " + body);

            ResponseEntity<TossPaymentResponse> response = new RestTemplate().exchange(
                    uri + request.getPaymentKey(),
                    HttpMethod.POST,
                    entity,
                    TossPaymentResponse.class
            );

            TossPaymentResponse toss = response.getBody();
            System.out.println("토스 API 응답: " + toss);


            // 임시 account → 추후 SecurityUtil.getAccountId() 로 변경
            Account account = accountRepository.findById(1L)
                    .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

            if (toss == null) {
                System.out.println("결제 응답 NULL");
                throw new BadRequestException("토스 결제 응답이 비어있습니다.");
            }

            // orderId 일치 여부 확인
            if (!request.getOrderId().equals(toss.getOrderId())) {
                System.out.println("주문번호 불일치: 요청=" + request.getOrderId() + ", 응답=" + toss.getOrderId());
            }


            Payment payment = Payment.builder()
                    .account(account)
                    .amount(BigDecimal.valueOf(toss.getAmount()))
                    .status(Payment.Status.SUCCESS)
                    .provider(Payment.Provider.TOSS)
                    .method(Payment.Method.CARD)  // 우선 CARD 고정
                    .transactionId(toss.getOrderId())
                    .updatedAt(OffsetDateTime.parse(toss.getApprovedAt()).toLocalDateTime())
                    .build();

            System.out.println("TOSS 응답: " + toss);  // orderId, paymentKey 등 포함
            System.out.println("저장할 PAYMENT: " + payment);

            Payment savedPayment = paymentRepository.save(payment);
            System.out.println("저장된 Payment ID: " + savedPayment.getId() + ", transactionId: " + savedPayment.getTransactionId());

            // 저장 직후 조회 확인
            Payment verifyPayment = paymentRepository.findByTransactionId(toss.getOrderId())
                    .orElse(null);
            System.out.println("저장 후 조회 결과: " + (verifyPayment != null ? "성공" : "실패"));

            return toss.getPaymentKey();


        } catch (HttpClientErrorException e) {
            System.out.println("토스 API 에러: " + e.getStatusCode() + ", " + e.getResponseBodyAsString());
            throw new BadRequestException("결제 실패: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("예상치 못한 에러: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("결제 처리 중 오류 발생: " + e.getMessage());
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