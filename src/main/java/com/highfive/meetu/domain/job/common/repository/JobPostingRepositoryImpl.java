package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 채용공고 커스텀 레포지토리 구현체
 */
@Repository
@RequiredArgsConstructor
public class JobPostingRepositoryImpl implements JobPostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * skills 리스트를 keyword에 포함하는 채용공고 조회 (랜덤 정렬, limit 설정)
     */
    @Override
    public List<JobPosting> findBySkillsInKeyword(List<String> skills, int limit) {
        QJobPosting jobPosting = QJobPosting.jobPosting;

        // skills 목록으로 OR 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        for (String skill : skills) {
            builder.or(jobPosting.keyword.containsIgnoreCase(skill));
        }

        // 랜덤 정렬 + limit 적용
        return queryFactory.selectFrom(jobPosting)
            .where(builder)
            .orderBy(Expressions.numberTemplate(Double.class, "rand()").asc())
            .limit(limit)
            .fetch();
    }
}

