package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.company.admin.dto.CompanyAdminDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.user.common.entity.QAccount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.highfive.meetu.domain.company.common.entity.QCompany.company;
import static com.highfive.meetu.domain.job.common.entity.QJobPosting.jobPosting;
import static com.highfive.meetu.domain.user.common.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class CompanyAdminRepository {

  private final JPAQueryFactory queryFactory;

  public Page<CompanyAdminDTO> findAllWithPagingByStatusAndKeywordAndSort(
      String statusFilter, String keyword, String sort, Pageable pageable) {

    BooleanBuilder condition = new BooleanBuilder();

    if ("pending".equals(statusFilter)) {
      condition.and(company.status.eq(0));
    } else if ("approved".equals(statusFilter)) {
      condition.and(company.status.in(1, 2));
    }

    if (keyword != null && !keyword.isBlank()) {
      condition.and(company.name.containsIgnoreCase(keyword));
    }

    // 🔽 정렬 조건
    OrderSpecifier<?> orderSpecifier = switch (sort) {
      case "foundedDate" -> company.foundedDate.asc();
      case "size"        -> company.numEmployees.desc();
      default            -> company.createdAt.desc();
    };

    List<Company> companies = queryFactory
        .selectFrom(company)
        .where(condition)
        .orderBy(orderSpecifier)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory
        .select(company.count())
        .from(company)
        .where(condition)
        .fetchOne();

    List<CompanyAdminDTO> dtoList = companies.stream().map(c -> {
      int postingCount = queryFactory.select(jobPosting.count())
          .from(jobPosting)
          .where(jobPosting.company.id.eq(c.getId()))
          .fetchOne().intValue();

      int managerCount = queryFactory.select(account.count())
          .from(account)
          .where(account.company.id.eq(c.getId()))
          .fetchOne().intValue();

      return CompanyAdminDTO.build(c, postingCount, managerCount);
    }).toList();

    return new PageImpl<>(dtoList, pageable, total);
  }

  public Company findById(Long id) {
    return queryFactory
        .selectFrom(company)
        .where(company.id.eq(id))
        .fetchOne();
  }
}
