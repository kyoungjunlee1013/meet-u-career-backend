package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.dto.JobCategoryOptionDTO;
import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;
import com.highfive.meetu.domain.job.common.entity.QJobCategory;
import com.highfive.meetu.domain.job.common.entity.QLocation;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobCategoryQueryRepositoryImpl implements JobCategoryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QJobCategory job = QJobCategory.jobCategory;

    /**
     * 키워드로 직무 카테고리 검색 (자동완성용)
     */
    @Override
    public List<JobCategoryOptionDTO> searchByKeyword(String keyword) {

        return queryFactory
                .select(Projections.constructor(JobCategoryOptionDTO.class,
                        job.id,
                        job.jobCode,
                        job.jobName))
                .from(job)
                .where(job.jobName.containsIgnoreCase(keyword))
                .orderBy(job.jobName.asc())
                .limit(20) // 너무 많은 결과 방지
                .fetch();
    }
}
