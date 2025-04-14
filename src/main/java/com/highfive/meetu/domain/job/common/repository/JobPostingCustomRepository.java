package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;

import java.util.List;

public interface JobPostingCustomRepository {
    List<JobPosting> searchByFilters(String jobType, Integer experienceLevel, Integer educationLevel, String locationCode);
}