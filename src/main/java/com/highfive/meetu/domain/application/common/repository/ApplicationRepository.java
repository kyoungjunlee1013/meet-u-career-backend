package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

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
