package com.highfive.meetu.domain.resume.personal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highfive.meetu.domain.resume.personal.dto.*;
import com.highfive.meetu.domain.resume.personal.service.ResumePersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personal/resume")
@RequiredArgsConstructor
public class ResumePersonalController {

    private final ResumePersonalService resumePersonalService;
    private final ObjectMapper objectMapper;

    // 이력서 초기 생성 메서드 - "이력서 작성" 버튼을 눌러서 이력서 작성 페이지로 넘어갈 때 데이터 생성
    @PostMapping("/init")
    public ResultData<Long> initResume(@RequestBody Map<String, Object> request) {
        Long profileId = Long.valueOf(request.get("profileId").toString());
        Integer resumeType = Integer.valueOf(request.get("resumeType").toString());

        Long resumeId = resumePersonalService.initResume(profileId, resumeType);
        return ResultData.success(1, resumeId);
    }

    // 이력서 유형: 파일 (resumeType = 1) 한 번에 생성
    // POST /api/personal/resume/file
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultData<Long> saveFileResume(
            @RequestPart("data") String data,
            @RequestPart("file") MultipartFile file
    ) throws JsonProcessingException {
        // JSON 문자열 → DTO 수동 파싱
        ResumeFileDTO dto = objectMapper.readValue(data, ResumeFileDTO.class);
        Long resumeId = resumePersonalService.saveFileResume(dto, file);
        return ResultData.success(1, resumeId);
    }


    // 기존 파일 이력서 수정 또는 init 이후 호출시
    @PatchMapping("/{resumeId}/file")
    public ResultData<String> updateFileResume(
            @PathVariable Long resumeId,
            @RequestPart("resumeFile") MultipartFile resumeFile
    ) {
        resumePersonalService.updateFileResume(resumeId, resumeFile);
        return ResultData.success(1, "파일 이력서가 저장되었습니다.");
    }



    // 이력서 유형: URL (resumeType = 2)
    // POST /api/personal/resume/url
    @PostMapping("/url")
    public ResultData<Long> saveUrlResume(
            @RequestBody ResumeUrlDTO dto
    ) {
        Long resumeId = resumePersonalService.saveUrlResume(dto);
        return ResultData.success(1, resumeId);
    }




    // 이력서 기본 정보 저장
    @PatchMapping("/{resumeId}/info")
    public ResultData<String> updateResumeBasicInfo(
            @PathVariable Long resumeId,
            @RequestPart("data") ResumeBasicInfoDTO dto,
            @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        resumePersonalService.updateResumeBasicInfo(resumeId, dto, resumeFile, profileImage);
        return ResultData.success(1, "이력서 기본 정보가 저장되었습니다.");
    }


    // 항목 추가 메서드
    @PostMapping("/{resumeId}/content")
    public ResultData<Long> addResumeContent(
            @PathVariable Long resumeId,
            @RequestPart("data") ResumeContentDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        dto.setResumeId(resumeId);
        Long contentId = resumePersonalService.saveResumeContent(dto, file);

        return ResultData.success(1, contentId);
    }


    // 항목 수정 메서드
    @PatchMapping("/{resumeId}/content/{contentId}")
    public ResultData<Long> updateResumeContent(
            @PathVariable Long resumeId,
            @PathVariable Long contentId,
            @RequestPart("data") ResumeContentDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        dto.setResumeId(resumeId);
        dto.setId(contentId);
        Long updatedId = resumePersonalService.updateResumeContent(dto, file);
        return ResultData.success(1, updatedId);
    }


    // 항목 삭제 메서드
    @DeleteMapping("/{resumeId}/content/{contentId}")
    public ResultData<String> deleteResumeContent(
            @PathVariable Long resumeId,
            @PathVariable Long contentId) {

        resumePersonalService.deleteResumeContent(resumeId, contentId);
        return ResultData.success(1, "이력서 항목이 삭제되었습니다.");
    }


    // 최종 "이력서 저장하기" 버튼 클릭시 전체 작성 내용이 저장되는 메서드
    @PostMapping("/{resumeId}/saveall")
    public ResultData<String> finalSaveResume(
            @PathVariable Long resumeId,
            @RequestPart("data") ResumeWriteRequestDTO dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestPart(value = "contentFiles", required = false) List<MultipartFile> contentFiles
    ) {
        // 파일들을 DTO에 주입
        dto.setProfileImage(profileImage);
        dto.setResumeFile(resumeFile);
        dto.setContentFiles(contentFiles);

        resumePersonalService.saveAllAtOnce(resumeId, dto);
        return ResultData.success(1, "이력서 전체 저장 완료");
    }




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
    @GetMapping("/view/{resumeId}")
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
    @PutMapping("/edit/{resumeId}")
    public ResultData<Long> updateResumeAll(@PathVariable Long resumeId, @RequestBody ResumePersonalDTO dto) {

        resumePersonalService.updateResumeAll(resumeId, dto);

        return ResultData.success(1, resumeId);
    }


}

