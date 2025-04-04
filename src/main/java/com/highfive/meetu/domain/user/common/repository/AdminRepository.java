package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
