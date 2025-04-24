package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.company.common.entity.CompanyFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyFollowRepository extends JpaRepository<CompanyFollow, Long> {

    List<CompanyFollow> findByAccount_Id(Long accountId);

}
