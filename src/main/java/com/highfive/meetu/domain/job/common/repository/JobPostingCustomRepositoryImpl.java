package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.entity.QLocation;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobPostingCustomRepositoryImpl implements JobPostingCustomRepository {

    private final JPAQueryFactory queryFactory;

    public JobPostingCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<JobPosting> searchByFilters(String jobType, Integer exp, Integer edu, String locationCode) {
        QJobPosting job = QJobPosting.jobPosting;
        QLocation loc = QLocation.location;

        return queryFactory
                .selectFrom(job)
                // location 연관 객체를 left join하여 함께 페치
                .leftJoin(job.location, loc).fetchJoin()
                .where(
                        eqJobType(jobType, job),
                        eqExperience(exp, job),
                        eqEducation(edu, job),
                        eqLocation(locationCode, loc)
                )
                .orderBy(job.createdAt.desc())
                .fetch();
    }

    private BooleanExpression eqJobType(String jobType, QJobPosting job) {
        return jobType != null ? job.jobType.eq(jobType) : null;
    }

    private BooleanExpression eqExperience(Integer exp, QJobPosting job) {
        return exp != null ? job.experienceLevel.eq(exp) : null;
    }

    private BooleanExpression eqEducation(Integer edu, QJobPosting job) {
        return edu != null ? job.educationLevel.eq(edu) : null;
    }

    private BooleanExpression eqLocation(String code, QLocation loc) {
        return code != null ? loc.locationCode.eq(code) : null;
    }
}

/*
MySQL 쿼리와 동등한 DSL 쿼리 (예시)

SELECT jp.*, l.locationCode, l.province, l.city, l.fullLocation
FROM job_posting jp
LEFT JOIN location l ON jp.locationCode = l.locationCode
WHERE (:jobType IS NULL OR jp.jobType = :jobType)
  AND (:exp IS NULL OR jp.experienceLevel = :exp)
  AND (:edu IS NULL OR jp.educationLevel = :edu)
  AND (:locationCode IS NULL OR l.locationCode = :locationCode)
ORDER BY jp.createdAt DESC;
*/
