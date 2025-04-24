package com.highfive.meetu.domain.community.admin.controller;

import com.highfive.meetu.domain.community.admin.dto.CommunityPostDTO;
import com.highfive.meetu.domain.community.admin.dto.CommunityPostSearchRequestDTO;
import com.highfive.meetu.domain.community.admin.service.CommunityPostAdminService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 커뮤니티 게시글 관리 API (관리자 전용)
 */
@RestController
@RequestMapping("/api/admin/community/posts")
@RequiredArgsConstructor
public class CommunityPostAdminController {

    private final CommunityPostAdminService postService;

    /**
     * 게시글 검색 (JSON 기반 필터 + 페이징 + 정렬)
     *
     * @param request 검색 조건 (status, page, size, sortBy, direction)
     */
    @PostMapping("/search")
    public ResultData<Page<CommunityPostDTO>> searchPosts(@RequestBody CommunityPostSearchRequestDTO request) {
        Page<CommunityPostDTO> posts = postService.searchPosts(request);
        return ResultData.success(1, posts);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{postId}")
    public ResultData<CommunityPostDTO> getPostDetail(@PathVariable Long postId) {
        CommunityPostDTO post = postService.getPostDetail(postId);
        return ResultData.success(1, post);
    }

    /**
     * 게시글 상태 토글 (활성/비활성)
     */
    @PatchMapping("/{postId}/status")
    public ResultData<Void> togglePostStatus(@PathVariable Long postId) {
        postService.togglePostStatus(postId);
        return ResultData.success(1, null);
    }
}
