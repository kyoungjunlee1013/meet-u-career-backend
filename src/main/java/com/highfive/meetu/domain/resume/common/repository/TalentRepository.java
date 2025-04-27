package com.highfive.meetu.domain.resume.common.repository;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 대표 이력서 조회용 리포지토리
 */
@Repository
public interface TalentRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByIsPrimaryTrue();
}
