package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.company.common.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
