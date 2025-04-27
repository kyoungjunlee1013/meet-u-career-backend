package com.highfive.meetu.domain.user.business.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.highfive.meetu.domain.user.business.dto.TalentResponseDto;
import com.highfive.meetu.domain.user.business.service.TalentService;
import com.highfive.meetu.global.common.response.ResultData;

/**
 * 비즈니스용 인재(이력서) 조회 REST API
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/business/talents")
public class TalentController {

    private final TalentService talentService;

    // 인재(대표 이력서) 목록 조회
    @GetMapping
    public ResultData<List<TalentResponseDto>> getTalents() {
        List<TalentResponseDto> dtos = talentService.findAllPrimaryResumes();
        return ResultData.success(dtos.size(), dtos);
    }
}
