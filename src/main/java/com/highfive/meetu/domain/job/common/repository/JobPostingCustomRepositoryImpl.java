package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.entity.QLocation;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingCustomRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JobPostingCustomRepositoryImpl implements JobPostingCustomRepository {

    private final JPAQueryFactory queryFactory;

    public JobPostingCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * industry(직무), exp(경력), edu(학력), locationCode(지역),
     * keyword(키워드 포함 검색), sort(newest|popular|recommended)
     */
    @Override
    public List<JobPosting> searchByFilters(
            String industry,
            Integer exp,
            Integer edu,
            String locationCode,
            String keyword,
            String sort
    ) {
        QJobPosting job = QJobPosting.jobPosting;
        QLocation loc = QLocation.location;

        // 공통 where 절
        BooleanExpression whereClause = job.isNotNull()  // 항상 true
                .and(eqIndustry(industry, job))
                .and(eqExperience(exp, job))
                .and(eqEducation(edu, job))
                .and(eqLocation(locationCode, loc))
                .and(eqKeyword(keyword, job));

        // 정렬 지정
        OrderSpecifier<?> orderSpecifier;
        switch (sort != null ? sort : "") {
            case "popular":
                orderSpecifier = job.viewCount.desc();
                break;
            case "recommended":
                orderSpecifier = job.applyCount.desc();
                break;
            default:
                orderSpecifier = job.createdAt.desc();
        }

        return queryFactory
                .selectFrom(job)
                .leftJoin(job.location, loc).fetchJoin()
                .where(whereClause)
                .orderBy(orderSpecifier)
                .fetch();
    }

    private BooleanExpression eqIndustry(String industry, QJobPosting job) {
        return industry != null && !industry.isBlank()
                ? job.industry.eq(industry)
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

    private BooleanExpression eqLocation(String code, QLocation loc) {
        return code != null && !code.isBlank()
                ? loc.locationCode.eq(code)
                : null;
    }

    private BooleanExpression eqKeyword(String keyword, QJobPosting job) {
        return keyword != null && !keyword.isBlank()
                ? job.keyword.like("%" + keyword + "%")
                : null;
    }
}

/*
MySQL 쿼리(예시)
------------------------------------------------------------
SELECT jp.*, l.locationCode, l.province, l.city, l.fullLocation
  FROM job_posting jp
  LEFT JOIN location l
    ON jp.locationCode = l.locationCode
 WHERE (:industry    IS NULL OR jp.industry        = :industry)
   AND (:exp         IS NULL OR jp.experienceLevel = :exp)
   AND (:edu         IS NULL OR jp.educationLevel  = :edu)
   AND (:locationCode IS NULL OR l.locationCode     = :locationCode)
   AND (:keyword     IS NULL OR jp.keyword LIKE CONCAT('%', :keyword, '%'))
 ORDER BY
   CASE
     WHEN :sort = 'popular'    THEN jp.viewCount
     WHEN :sort = 'recommended' THEN jp.applyCount
     ELSE jp.createdAt
   END DESC;
------------------------------------------------------------
*/

