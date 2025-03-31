package com.highfive.meetu.domain.resume.common.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ResumeQueryRepositoryImpl implements ResumeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

}
