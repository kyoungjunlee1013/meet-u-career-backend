package com.highfive.meetu.domain.resume.personal.controller;

import com.highfive.meetu.domain.resume.personal.dto.ResumePersonalDTO;
import com.highfive.meetu.domain.resume.personal.service.ResumePersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/resume")
@RequiredArgsConstructor
public class ResumePersonalController {

    private final ResumePersonalService resumePersonalService;

    /**
     * 내 이력서 목록 조회
     *
     * @param profileId 로그인한 사용자의 프로필 ID
     * @return ResultData (count, data)
     */
    @GetMapping("/list/{profileId}")
    public ResultData<List<ResumePersonalDTO>> getMyResumeList(@PathVariable Long profileId) {

        // 서비스 단에서 profileId를 기반으로 해당 사용자의 이력서 리스트를 조회
        List<ResumePersonalDTO> resumeList = resumePersonalService.getResumeListByProfileId(profileId);

        // ResultData 포맷으로 응답 반환 (count: 리스트 크기, data: 리스트)
        return ResultData.success(resumeList.size(), resumeList);
    }


    /**
     * 이력서 상세 조회
     *
     * @param resumeId 조회할 이력서 ID
     * @return 이력서 상세 정보
     */
    @GetMapping("/{resumeId}")
    public ResultData<ResumePersonalDTO> getResumeDetail(@PathVariable Long resumeId) {

        ResumePersonalDTO resumeDetail = resumePersonalService.getResumeDetail(resumeId);
        return ResultData.success(1, resumeDetail);
    }

    /**
     * 이력서 작성 (임시저장 또는 최초 생성)
     *
     * @param dto 작성할 이력서 정보 (제목, 개요 등 최소 정보 포함)
     * @return ResultData(1, 생성된 resumeId)
     */
    @PostMapping("/create")
    public ResultData<Long> createResume(@RequestBody ResumePersonalDTO dto) {
        Long resumeId = resumePersonalService.createResume(dto);
        return ResultData.success(1, resumeId); // ID만 응답
    }


    /**
     * 이력서 전체 수정
     * - 이력서 기본 정보(제목, 개요 등) + 항목 리스트(ResumeContent) 모두 수정
     *
     * @param resumeId 수정 대상 이력서 ID
     * @param dto 수정할 내용이 포함된 ResumePersonalDTO
     * @return 수정된 이력서 ID
     */
    @PatchMapping("/{resumeId}")
    public ResultData<Long> updateResumeAll(@PathVariable Long resumeId, @RequestBody ResumePersonalDTO dto) {

        resumePersonalService.updateResumeAll(resumeId, dto);

        return ResultData.success(1, resumeId);
    }


}

