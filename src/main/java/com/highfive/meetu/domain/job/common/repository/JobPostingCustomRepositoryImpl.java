package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.entity.QLocation;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobPostingCustomRepositoryImpl implements JobPostingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<JobPosting> searchByFilters(
            List<String> industry,
            Integer exp,
            Integer edu,
            List<String> locationCodes,
            String keyword,
            String sort,
            Pageable pageable
    ) {
        QJobPosting job = QJobPosting.jobPosting;
        QLocation loc = QLocation.location;

        LocalDate today = LocalDate.now();

        // 공통 where 절
        BooleanExpression whereClause = job.isNotNull()
                .and(eqIndustry(industry, job))
                .and(eqExperience(exp, job))
                .and(eqEducation(edu, job))
                .and(eqLocation(locationCodes, loc))
                .and(eqKeyword(keyword, job))
                .and(job.expirationDate.goe(today.atStartOfDay()));

        // 정렬 조건
        OrderSpecifier<?> orderSpecifier;
        switch (sort != null ? sort : "") {
            case "popular":
                orderSpecifier = job.viewCount.desc();
                break;
            case "recommended":
                orderSpecifier = job.expirationDate.asc();
                break;
            default:
                orderSpecifier = job.createdAt.desc();
        }

        // 데이터 조회 (with 페이징)
        List<JobPosting> content = queryFactory
                .selectFrom(job)
                .leftJoin(job.location, loc).fetchJoin()
                .where(whereClause)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리 별도 실행
        Long total = queryFactory
                .select(job.count())
                .from(job)
                .where(whereClause)
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> total != null ? total : 0);
    }

    // ------------------------- where 조건 메서드 -------------------------

    private BooleanExpression eqIndustry(List<String> industryList, QJobPosting job) {
        return (industryList != null && !industryList.isEmpty())
                ? job.industry.in(industryList)
                : null;
    }

    private BooleanExpression eqExperience(Integer exp, QJobPosting job) {
        return exp != null
                ? job.experienceLevel.eq(exp)
                : null;
    }

    private BooleanExpression eqEducation(Integer edu, QJobPosting job) {
        return edu != null
                ? job.educationLevel.eq(edu)
                : null;
    }

    private BooleanExpression eqLocation(List<String> codes, QLocation loc) {
        return (codes != null && !codes.isEmpty())
                ? loc.locationCode.in(codes)
                : null;
    }

    private BooleanExpression eqKeyword(String keyword, QJobPosting job) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        // 검색어 2글자 이상일 때만 적용
        if (keyword.length() < 2) {
            return null;
        }

        return job.keyword.containsIgnoreCase(keyword)
                .or(job.title.containsIgnoreCase(keyword));
    }
}
