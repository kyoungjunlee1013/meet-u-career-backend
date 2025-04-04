package com.highfive.meetu.domain.coverletter.common.repository;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
}
