package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @Query("SELECT a.jobPosting.id, COUNT(a) FROM application a WHERE a.jobPosting.company.id = :companyId GROUP BY a.jobPosting.id")
  List<Object[]> countApplicationsByJobPostingGrouped(@Param("companyId") Long companyId);

  default Map<Long, Integer> countApplicationsGroupByJobPosting(Long companyId) {
    List<Object[]> results = countApplicationsByJobPostingGrouped(companyId);
    Map<Long, Integer> map = new HashMap<>();
    for (Object[] row : results) {
      map.put((Long) row[0], ((Long) row[1]).intValue());
    }
    return map;
  }
}
