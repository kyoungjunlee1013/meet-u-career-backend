package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobPostingCustomRepository {
    Page<JobPosting> searchByFilters(
            List<String> industry,
            Integer experienceLevel,
            Integer educationLevel,
            List<String> locationCode,
            String keyword,
            String sort,
            Pageable pageable
    );
}