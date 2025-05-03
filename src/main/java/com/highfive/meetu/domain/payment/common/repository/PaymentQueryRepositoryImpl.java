package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.company.common.entity.QCompany;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.payment.common.entity.Payment;
import com.highfive.meetu.domain.payment.common.entity.QAdvertisement;
import com.highfive.meetu.domain.payment.common.entity.QPayment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QPayment payment = QPayment.payment;
    private final QAdvertisement ad = QAdvertisement.advertisement;
    private final QJobPosting job = QJobPosting.jobPosting;
    private final QCompany company = QCompany.company;

    @Override
    public Page<Payment> searchByFilters(String status, String startDate, String endDate,
                                         String searchType, String search, Pageable pageable) {


        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(status)) {
            builder.and(payment.status.eq(Integer.parseInt(status)));
        }

        if (StringUtils.hasText(startDate)) {
            builder.and(payment.createdAt.goe(LocalDate.parse(startDate).atStartOfDay()));
        }

        if (StringUtils.hasText(endDate)) {
            builder.and(payment.createdAt.loe(LocalDate.parse(endDate).atTime(LocalTime.MAX)));
        }

        if (StringUtils.hasText(searchType) && StringUtils.hasText(search)) {
            switch (searchType) {
                case "transactionId" -> builder.and(payment.transactionId.containsIgnoreCase(search));
                case "userName" -> builder.and(payment.account.name.containsIgnoreCase(search));
                case "companyName" -> builder.and(ad.company.name.containsIgnoreCase(search));
            }
        }

        JPAQuery<Payment> query = queryFactory
                .selectFrom(payment)
                .leftJoin(payment.advertisement, ad).fetchJoin()
                .leftJoin(ad.jobPosting, job).fetchJoin()
                .leftJoin(ad.company, company).fetchJoin()
                .where(builder)
                .orderBy(payment.createdAt.desc());

        long total = query.fetch().size(); // 또는 fetchCount() 대체

        List<Payment> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

}
