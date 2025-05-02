package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, LocationQueryRepository {

    List<Location> findAllByOrderByIdAsc();
    Optional<Location> findByLocationCode(String locationCode);
    List<Location> findByProvince(String province);
}
