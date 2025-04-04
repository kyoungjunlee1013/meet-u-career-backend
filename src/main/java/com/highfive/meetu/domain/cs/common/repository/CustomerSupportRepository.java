package com.highfive.meetu.domain.cs.common.repository;

import com.highfive.meetu.domain.cs.common.entity.CustomerSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSupportRepository extends JpaRepository<CustomerSupport, Long> {

}
