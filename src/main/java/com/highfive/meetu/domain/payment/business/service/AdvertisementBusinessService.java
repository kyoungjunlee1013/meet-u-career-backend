package com.highfive.meetu.domain.payment.business.service;

import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.payment.business.dto.AdvertisementRegisterRequest;
import com.highfive.meetu.domain.payment.business.dto.TossPaymentConfirmRequest;
import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import com.highfive.meetu.domain.payment.common.repository.AdvertisementRepository;
import com.highfive.meetu.domain.payment.common.repository.PaymentRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AdvertisementBusinessService {

    private final AdvertisementRepository advertisementRepository;
    private final PaymentRepository  paymentRepository;
    private final PaymentBusinessService paymentBusinessService;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    public ResultData<Long> registerAdWithPayment(Long companyId, AdvertisementRegisterRequest request) {

        // Toss 결제 승인
        TossPaymentConfirmRequest confirmRequest = new TossPaymentConfirmRequest(
                request.getPaymentKey(), request.getOrderId(), request.getAmount()
        );
        String paymentKey = paymentBusinessService.confirmPayment(confirmRequest);

        // 결제 객체 조회
        Payment payment = paymentRepository.findByTransactionId(paymentKey)
                .orElseThrow(() -> new NotFoundException("결제 정보 없음"));

        // 공고 조회
        JobPosting jobPosting = jobPostingRepository.findById(request.getJobPostingId())
                .orElseThrow(() -> new NotFoundException("공고 정보 없음"));

        // 광고 등록
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(request.getDurationDays());

        Advertisement ad = Advertisement.builder()
                .company(companyRepository.getReferenceById(companyId))
                .jobPosting(jobPosting)
                .payment(payment)
                .adType(request.getAdType())               // BASIC / STANDARD / PREMIUM
                .durationDays(request.getDurationDays())   // 3일, 7일 등
                .status(Advertisement.Status.ACTIVE)
                .startDate(now)
                .endDate(end)
                .build();

        advertisementRepository.save(ad);
        return ResultData.success(1, ad.getId());
    }


}
