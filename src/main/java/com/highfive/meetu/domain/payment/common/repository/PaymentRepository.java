package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.payment.common.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
