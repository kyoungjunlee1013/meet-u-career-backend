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

  /**
   * 채용공고 ID로 지원서 목록 조회
   */
  @Query("""
        SELECT a
        FROM application a
        JOIN FETCH a.profile p
        WHERE a.jobPosting.id = :jobPostingId
          AND a.status != 4
    """)
  List<Application> findAllByJobPostingId(@Param("jobPostingId") Long jobPostingId);

  /**
   * 프로필과 채용공고를 기준으로 지원 여부 확인
   */
  boolean existsByProfileAndJobPosting(Profile profile, JobPosting jobPosting);

  /**
   * ✅ 추가: 계정 ID로 지원서 목록 조회
   *
   * Profile → Account → Id를 타고 검색
   */
  List<Application> findByProfile_Account_Id(Long accountId);

  /**
   * ✅ 추가: 기업 ID 기준으로 공고별 지원자 수 집계 (List<Object[]>로 반환)
   */
  @Query("""
        SELECT a.jobPosting.id, COUNT(a)
        FROM application a
        WHERE a.jobPosting.company.id = :companyId
        GROUP BY a.jobPosting.id
    """)
  List<Object[]> countApplicationsByJobPostingGrouped(@Param("companyId") Long companyId);

  /**
   * ✅ 추가: 위 결과를 Map<Long, Integer> 형태로 변환해서 반환
   */
  default Map<Long, Integer> countApplicationsGroupByJobPosting(Long companyId) {
    List<Object[]> results = countApplicationsByJobPostingGrouped(companyId);
    Map<Long, Integer> map = new HashMap<>();
    for (Object[] row : results) {
      map.put((Long) row[0], ((Long) row[1]).intValue());
    }
    return map;
  }
}
