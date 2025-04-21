package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.personal.dto.ApplicationPersonalDTO;
import java.util.List;

public interface ApplicationQueryRepository {
    List<ApplicationPersonalDTO> findAllByProfileId(Long profileId);
}
