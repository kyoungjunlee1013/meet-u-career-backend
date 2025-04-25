package com.highfive.meetu.domain.resume.personal.controller;

import com.highfive.meetu.domain.application.personal.service.ApplicationPersonalService;
import com.highfive.meetu.domain.resume.personal.dto.ResumePersonalDTO;
import com.highfive.meetu.domain.resume.personal.service.ResumePersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/resume")
@RequiredArgsConstructor
@Tag(name = "resume-personal-controller", description = "개인회원 이력서 관련 API")
public class ResumePersonalController {

    private final ResumePersonalService resumePersonalService;
    private final ApplicationPersonalService applicationPersonalService;


    /**
     * 이력서 목록 조회
     */
    @GetMapping("/list")
    public ResultData<List<ResumePersonalDTO>> getMyResumeList() {
        Long profileId = SecurityUtil.getProfileId();
        List<ResumePersonalDTO> resumeList = resumePersonalService.getResumeListByProfileId(profileId);
        return ResultData.success(resumeList.size(), resumeList);
    }

    /**
     * 입사 지원
     * @param jobPostingId 채용 공고 ID
     * @param resumeId 이력서 ID
     * @return 결과 메시지
     */
    @PostMapping("/apply")
    public ResultData<?> applyForJob(@RequestParam Long jobPostingId, @RequestParam Long resumeId) {
        Long profileId = SecurityUtil.getProfileId();  // 로그인한 사용자의 프로필 ID 가져오기
        applicationPersonalService.applyForJob(profileId, jobPostingId, resumeId);
        return ResultData.success(1, "입사 지원이 완료되었습니다.");
    }

}

