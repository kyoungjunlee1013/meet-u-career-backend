package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;
import com.highfive.meetu.domain.job.common.entity.QLocation;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationQueryRepositoryImpl implements LocationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QLocation location = QLocation.location;

    public List<LocationOptionDTO> findProvincesForDropdown() {

        return queryFactory
                .select(Projections.constructor(LocationOptionDTO.class,
                        location.locationCode.min(), // 대표 locationCode 하나만
                        location.province))
                .from(location)
                .where(location.province.isNotNull()
                        .and(location.province.notIn("전국", "베트남", "인도")))
                .groupBy(location.province) // province 단위로 하나씩만
                .fetch();
    }
}
