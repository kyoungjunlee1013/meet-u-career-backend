package com.highfive.meetu.domain.resume.common.repository;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeContentRepository extends JpaRepository<ResumeContent, Long> {


    void deleteAllByResumeId(Long resumeId);
}
