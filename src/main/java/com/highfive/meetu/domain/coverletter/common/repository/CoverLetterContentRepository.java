package com.highfive.meetu.domain.coverletter.common.repository;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetterContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverLetterContentRepository extends JpaRepository<CoverLetterContent, Long> {

}
