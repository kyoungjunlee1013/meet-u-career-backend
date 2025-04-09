package com.highfive.meetu.domain.coverletter.common.repository;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetterContent;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetterContentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoverLetterContentFeedbackRepository extends JpaRepository<CoverLetterContentFeedback, Long> {

    Optional<CoverLetterContentFeedback> findByContent(CoverLetterContent content);

}
