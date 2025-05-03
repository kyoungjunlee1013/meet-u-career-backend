package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.company.common.entity.QCompany;
import com.highfive.meetu.domain.job.admin.dto.JobPostingAdminDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobPostingAdminRepositoryImpl implements JobPostingAdminRepository {

  private final JPAQueryFactory queryFactory;
  private static final QJobPosting jobPosting = QJobPosting.jobPosting;
  private static final QCompany company = QCompany.company;

  @Override
  public Page<JobPostingAdminDTO> findAllByStatus(Pageable pageable, Integer status) {
    QueryResults<JobPostingAdminDTO> results = queryFactory
        .select(Projections.constructor(
            JobPostingAdminDTO.class,
            jobPosting.id,
            jobPosting.title,
            company.name,
            jobPosting.createdAt.stringValue().substring(0, 10),
            jobPosting.expirationDate.stringValue().substring(0, 10),
            jobPosting.viewCount,
            jobPosting.applyCount,
            jobPosting.status.stringValue(),
            jobPosting.description
        ))
        .from(jobPosting)
        .leftJoin(jobPosting.company, company)
        .where(
            status != null ? jobPosting.status.eq(status) : null
        )
        .orderBy(jobPosting.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<JobPostingAdminDTO> content = results.getResults();
    long total = results.getTotal();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public void approve(Long id) {
    queryFactory
        .update(jobPosting)
        .set(jobPosting.status, JobPosting.Status.ACTIVE)
        .where(jobPosting.id.eq(id))
        .execute();
  }

  @Override
  public void reject(Long id) {
    queryFactory
        .update(jobPosting)
        .set(jobPosting.status, JobPosting.Status.INACTIVE)
        .where(jobPosting.id.eq(id))
        .execute();
  }

  @Override
  public void block(Long id) {
    queryFactory
        .update(jobPosting)
        .set(jobPosting.status, JobPosting.Status.INACTIVE)
        .where(jobPosting.id.eq(id))
        .execute();
  }
}
