package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long>, JobCategoryQueryRepository {

    // 단일 직무 코드로 조회
    Optional<JobCategory> findByJobCode(String jobCode);

}
