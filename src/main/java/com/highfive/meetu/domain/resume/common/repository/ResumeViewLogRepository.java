package com.highfive.meetu.domain.resume.common.repository;

import com.highfive.meetu.domain.resume.common.entity.ResumeViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeViewLogRepository extends JpaRepository<ResumeViewLog, Long> {

}
