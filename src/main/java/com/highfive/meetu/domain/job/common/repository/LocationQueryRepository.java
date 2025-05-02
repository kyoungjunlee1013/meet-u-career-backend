package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;

import java.util.List;

public interface LocationQueryRepository {

    List<LocationOptionDTO> findProvincesForDropdown();

    List<LocationOptionDTO> findCitiesByProvince(String province);
}
