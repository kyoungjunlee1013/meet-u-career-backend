package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationRepositoryCustom {
}
