package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.entity.Location;

import java.util.List;
import java.util.Optional;

public interface JobPostingCustomRepository {
    List<JobPosting> searchByFilters(
            List<String> industry,
            Integer exp,
            Integer edu,
            List<String> locationCodes,
            String keyword,
            String sort
    );

}