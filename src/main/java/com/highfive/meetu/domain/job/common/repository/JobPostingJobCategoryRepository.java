package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobPostingJobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingJobCategoryRepository extends JpaRepository<JobPostingJobCategory, Long> {

}
