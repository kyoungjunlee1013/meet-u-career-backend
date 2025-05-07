package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.repository.ResumeRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 이력서 다운로드 서비스
 */
@Service
@RequiredArgsConstructor
public class ResumeDownloadService {

    private final ResumeRepository resumeRepository;
    private final ResumePdfGenerator pdfGenerator;

    public void downloadResumeAsPdf(Long resumeId, HttpServletResponse response) throws IOException {
        Resume resume = resumeRepository.findWithResumeContentsOnly(resumeId)
            .orElseThrow(() -> new NotFoundException("해당 이력서를 찾을 수 없습니다."));

        if (resume.getCoverLetter() != null) {
            Long coverLetterId = resume.getCoverLetter().getId();
            resume.setCoverLetter(
                resumeRepository.findCoverLetterWithContents(coverLetterId)
                    .orElse(null)
            );
        }

        pdfGenerator.generateResumePdf(resume, response);
    }
}