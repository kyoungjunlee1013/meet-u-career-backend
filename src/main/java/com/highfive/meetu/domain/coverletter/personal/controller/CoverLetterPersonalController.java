package com.highfive.meetu.domain.coverletter.personal.controller;

import com.highfive.meetu.domain.coverletter.personal.dto.CoachingRequestDTO;
import com.highfive.meetu.domain.coverletter.personal.dto.CoachingResponseDTO;
import com.highfive.meetu.domain.coverletter.personal.dto.CoverLetterPersonalDTO;
import com.highfive.meetu.domain.coverletter.personal.service.CoachingService;
import com.highfive.meetu.domain.coverletter.personal.service.CoverLetterPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/coverletter")
@RequiredArgsConstructor
public class CoverLetterPersonalController {

    private final CoachingService coachingService;
    private final CoverLetterPersonalService coverLetterPersonalService;

    // 자기소개서 목록
    @GetMapping("/list")
    public ResultData<List<CoverLetterPersonalDTO>> getMyCoverLetters() {
        Long profileId = 1L;
        //Long profileId = SecurityUtil.getProfileId(); // 로그인 사용자 기준

        List<CoverLetterPersonalDTO> result = coverLetterPersonalService.getCoverLetterList(profileId);
        return ResultData.success(result.size(), result);
    }


    @PostMapping("/coaching")
    public ResultData<CoachingResponseDTO> getCoaching(@RequestBody CoachingRequestDTO dto) {
        CoachingResponseDTO response = coachingService.getCoaching(
                dto.getContentId(),
                dto.getSectionTitle(),
                dto.getContent()
        );

        return ResultData.success(1, response);
    }
}
