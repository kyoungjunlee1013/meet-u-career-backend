package com.highfive.meetu.domain.system.common.repository;

import com.highfive.meetu.domain.system.common.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

}
