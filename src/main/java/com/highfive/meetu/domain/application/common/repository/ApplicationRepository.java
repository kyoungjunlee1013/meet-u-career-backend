package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.dashboard.personal.dto.RecentApplicationDTO;
import com.highfive.meetu.domain.dashboard.personal.dto.ApplicationSummaryDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationRepositoryCustom {

    @Query("""
        SELECT a
        FROM application a
        JOIN FETCH a.profile p
        WHERE a.jobPosting.id = :jobPostingId
          AND a.status != 4
    """)
    List<Application> findAllByJobPostingId(@Param("jobPostingId") Long jobPostingId);

    boolean existsByProfileAndJobPosting(Profile profile, JobPosting jobPosting);

  @Query("SELECT new com.highfive.meetu.domain.dashboard.personal.dto.RecentApplicationDTO(" +
      "c.name, jp.title, a.status) " +
      "FROM application a " +  // ✅ 클래스명으로 변경
      "JOIN a.jobPosting jp " +
      "JOIN jp.company c " +
      "WHERE a.profile.id = :profileId " +
      "ORDER BY a.createdAt DESC")
  List<RecentApplicationDTO> findRecentByProfileId(@Param("profileId") Long profileId);

  @Query("SELECT new com.highfive.meetu.domain.dashboard.personal.dto.ApplicationSummaryDTO(" +
      "SUM(CASE WHEN a.status = 1 THEN 1 ELSE 0 END), " +
      "SUM(CASE WHEN a.status = 2 THEN 1 ELSE 0 END), " +
      "SUM(CASE WHEN a.status = 3 THEN 1 ELSE 0 END), " +
      "SUM(CASE WHEN a.status = 4 THEN 1 ELSE 0 END)) " +
      "FROM application a " +  // ✅ 클래스명으로 변경
      "WHERE a.profile.id = :profileId")
  ApplicationSummaryDTO aggregateStatusSummary(@Param("profileId") Long profileId);
}
