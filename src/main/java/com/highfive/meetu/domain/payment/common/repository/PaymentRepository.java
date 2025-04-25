package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.payment.common.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentQueryRepository {

    // 내림차순으로 기업회원 결제내역 조회
    List<Payment> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);

    // 결제 키(고유 거래 ID)로 조회
    Optional<Payment> findByTransactionId(String transactionId);
}
