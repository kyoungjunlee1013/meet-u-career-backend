package com.highfive.meetu.domain.community.admin.controller;

import com.highfive.meetu.domain.community.admin.dto.CommunityTagDTO;
import com.highfive.meetu.domain.community.admin.dto.CreateCommunityTagRequest;
import com.highfive.meetu.domain.community.admin.dto.TagSearchRequestDTO;
import com.highfive.meetu.domain.community.admin.service.CommunityTagAdminService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 커뮤니티 태그 관리 API (관리자 전용)
 */
@RestController
@RequestMapping("/api/admin/community/tags")
@RequiredArgsConstructor
public class CommunityTagAdminController {

    private final CommunityTagAdminService tagService;

    /**
     * 태그 생성
     * @param request 태그 생성 요청 DTO
     * @return 생성된 태그 ID
     */
    @PostMapping
    public ResultData<Long> createTag(@RequestBody CreateCommunityTagRequest request) {
        Long tagId = tagService.createTag(request.getName());
        return ResultData.success(1, tagId);
    }

    /**
     * 태그 전체 조회
     * @param request 태그 조회 요청 DTO
     * @return 태그 리스트
     */
    @PostMapping("/search")
    public ResultData<Page<CommunityTagDTO>> searchTags(@RequestBody TagSearchRequestDTO request) {
        Page<CommunityTagDTO> tagPage = tagService.getTags(request);
        return ResultData.success(1, tagPage);
    }

    /**
     * 태그 상태 토글 (활성 ↔ 비활성)
     * @param tagId 태그 ID
     * @return 성공 여부
     */
    @PatchMapping("/{tagId}/status")
    public ResultData<Void> toggleStatus(@PathVariable Long tagId) {
        tagService.toggleStatus(tagId);
        return ResultData.success(1, null);
    }
}
