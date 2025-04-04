package com.highfive.meetu.domain.company.common.repository;

import com.highfive.meetu.domain.company.common.entity.CompanyFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyFollowRepository extends JpaRepository<CompanyFollow, Long> {

}
