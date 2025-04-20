package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.dto.JobCategoryOptionDTO;
import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;

import java.util.List;

public interface JobCategoryQueryRepository {

    List<JobCategoryOptionDTO> searchByKeyword(String keyword);
}
