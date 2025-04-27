package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityPostSimpleDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityPopularPostService;
import com.highfive.meetu.domain.community.personal.service.CommunityPostService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/popular/posts")
public class CommunityPopularPostController {

  private final CommunityPostService communityPostService;
  private final CommunityPopularPostService communityPopularPostService;

  /**
   * 전체 인기 게시글 조회 (해시태그별로 1개씩 뽑아서)
   * GET /api/community/popular/posts/all
   */
  @GetMapping("/all")
  public ResultData<List<CommunityPostSimpleDTO>> getPopularPosts(@RequestParam(defaultValue = "5") int limit) {
    List<CommunityPostSimpleDTO> popularPosts = communityPostService.getPopularPosts(limit);
    return ResultData.success(popularPosts.size(), popularPosts);
  }

  /**
   * 특정 해시태그별 인기 게시글 조회
   * GET /api/community/popular/posts/by-tag/{tagId}?limit=5
   */
  @GetMapping("/by-tag/{tagId}") // ✨ 수정
  public ResultData<List<CommunityPostSimpleDTO>> getPopularPostsByTag(@PathVariable Long tagId,
                                                                       @RequestParam(defaultValue = "5") int limit) {
    List<CommunityPostSimpleDTO> popularPosts = communityPostService.getPopularPostsByTag(tagId, limit);
    return ResultData.success(popularPosts.size(), popularPosts);
  }

  /**
   * 단일 인기 게시글 조회
   */
  @GetMapping("/{postId}")
  public ResultData<CommunityPostSimpleDTO> getPostDetail(@PathVariable Long postId) {
    CommunityPostSimpleDTO post = communityPostService.findSimplePostById(postId);
    return ResultData.success(1, post);
  }
}
