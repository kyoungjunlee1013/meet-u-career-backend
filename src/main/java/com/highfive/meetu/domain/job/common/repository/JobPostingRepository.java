package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.dashboard.personal.dto.RecommendedJobPostingDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
  @Query("SELECT new com.highfive.meetu.domain.dashboard.personal.dto.RecommendedJobPostingDTO(" +
      "c.name, jp.title, l.fullLocation, jp.salaryRange, jp.expirationDate, jp.keyword) " +
      "FROM jobPosting jp " +
      "JOIN jp.company c " +
      "JOIN jp.location l " +
      "WHERE jp.status = 2 " +
      "ORDER BY jp.expirationDate ASC")
  List<RecommendedJobPostingDTO> findRecommendedForProfile(@Param("profile") Profile profile);




}
