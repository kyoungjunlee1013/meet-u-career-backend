package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.payment.common.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentQueryRepository {

    Page<Payment> searchByFilters(
            String status,
            String startDate,
            String endDate,
            String searchType,
            String search,
            Pageable pageable
    );
}
