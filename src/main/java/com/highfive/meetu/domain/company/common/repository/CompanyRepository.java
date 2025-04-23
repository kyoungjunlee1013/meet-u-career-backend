package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  @Query("SELECT new com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO(" +
      "c.id, c.name, COUNT(r), c.industry, c.logoKey, c.address, c.businessNumber, c.website) " +
      "FROM interviewReview r JOIN r.company c " +
      "WHERE r.status = 0 " +
      "GROUP BY c.id, c.name, c.industry, c.logoKey, c.address, c.businessNumber, c.website " +
      "ORDER BY COUNT(r) DESC")
  List<InterviewCompanySummaryDTO> findCompaniesWithReviews();

}
