package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.company.common.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Company(기업) 관련 JPA Repository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  /**
   * 회사명으로 회사 조회
   */
  Optional<Company> findByName(String name);

  /**
   * 사업자등록번호로 회사 조회
   */
  Optional<Company> findByBusinessNumber(String businessNumber);

  /**
   * 상태(status) 기준으로 회사 목록 조회
   */
  List<Company> findAllByStatus(Integer status);

  /**
   * ✅ (추가) 계정(accountId) 기반으로 회사 조회
   *
   * accountList를 join 해서 accountId로 찾는다.
   */
  @Query("SELECT c FROM company c JOIN c.accountList a WHERE a.id = :accountId")
  Optional<Company> findByAccountId(@Param("accountId") Long accountId);
}
