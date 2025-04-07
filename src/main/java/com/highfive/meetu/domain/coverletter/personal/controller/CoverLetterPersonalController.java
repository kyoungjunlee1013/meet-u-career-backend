package com.highfive.meetu.domain.coverletter.personal.controller;

import com.highfive.meetu.domain.coverletter.personal.dto.CoachingRequest;
import com.highfive.meetu.domain.coverletter.personal.dto.CoachingResponse;
import com.highfive.meetu.domain.coverletter.personal.service.CoachingService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personal/coverletter")
@RequiredArgsConstructor
public class CoverLetterPersonalController {

    private final CoachingService coachingService;

    @PostMapping("/coaching")
    public ResultData<CoachingResponse> getCoaching(@RequestBody CoachingRequest dto) {
        CoachingResponse response = coachingService.getCoaching(
                dto.getContentId(),
                dto.getSectionTitle(),
                dto.getContent()
        );

        return ResultData.success(1, response);
    }
}
