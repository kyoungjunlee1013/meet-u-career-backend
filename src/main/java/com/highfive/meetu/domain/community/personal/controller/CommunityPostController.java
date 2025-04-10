package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityNewsDTO;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityNewsService;
import com.highfive.meetu.domain.community.personal.service.CommunityPostService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

  private final CommunityPostService communityPostService;
  private final CommunityNewsService communityNewsService;

  /**
   * [POST] 커뮤니티 게시글 등록
   */
  @PostMapping
  public ResultData<CommunityPostDTO> createPost(@RequestBody CommunityPostDTO dto) {
    CommunityPostDTO result = communityPostService.createPost(dto);
    return ResultData.success(1, result);
  }

  /**
   * [GET] 커뮤니티 게시글 목록 조회 (최신순)
   */
  @GetMapping
  public ResultData<Map<String, Object>> getAllActivePosts() {
    List<CommunityPostDTO> result = communityPostService.getAllActivePosts();

    // tagId가 null이면 기본값 4 (취업) 사용
    List<CommunityNewsDTO> news = communityNewsService.getNewsByTagId(4L);
    // posts + news 를 하나의 Map으로 묶음
    Map<String, Object> response = new HashMap<>();
    response.put("posts", result);
    response.put("news", news);

    return ResultData.success(result.size(), response);
  }

  /**
   * [GET] 게시글 상세 조회
   */
  @GetMapping("/{postId}")
  public ResultData<CommunityPostDTO> getPostDetail(@PathVariable Long postId) {
    CommunityPostDTO result = communityPostService.getPostDetail(postId);
    return ResultData.success(1, result);
  }

  /**
   * [PUT] 게시글 수정
   */
  @PutMapping
  public ResultData<CommunityPostDTO> updatePost(@RequestBody CommunityPostDTO dto) {
    CommunityPostDTO result = communityPostService.updatePost(dto);
    return ResultData.success(1, result);
  }

  /**
   * [DELETE] 게시글 삭제 (Soft Delete)
   */
  @DeleteMapping("/{postId}")
  public ResultData<String> deletePost(@PathVariable Long postId) {
    communityPostService.deletePost(postId);
    return ResultData.success(1, "게시글이 삭제되었습니다.");
  }


  /**
   * [GET] 인기글 조회 (전체 기준)
   * 예: /api/community/posts/popular?limit=5
   */
  @GetMapping("/popular")
  public ResultData<List<CommunityPostDTO>> getPopularPosts(
      @RequestParam(defaultValue = "5") int limit
  ) {
    List<CommunityPostDTO> result = communityPostService.getPopularPosts(limit);
    return ResultData.success(result.size(), result);
  }

  /**
   * [GET] 인기글 조회 (해시태그 기준)
   * 예: /api/community/posts/popular/tag/3?limit=5
   */
  @GetMapping("/popular/tag/{tagId}")
  public ResultData<List<CommunityPostDTO>> getPopularPostsByTag(
      @PathVariable Long tagId,
      @RequestParam(defaultValue = "5") int limit
  ) {
    List<CommunityPostDTO> result = communityPostService.getPopularPostsByTag(tagId, limit);
    return ResultData.success(result.size(), result);
  }

  /**
   * 내가 쓴글 조회
   */

  @GetMapping("/my/{accountId}")
  public ResultData<List<CommunityPostDTO>> getMyPosts(@PathVariable Long accountId) {
    List<CommunityPostDTO> result = communityPostService.getMyPosts(accountId);
    return ResultData.success(result.size(), result);
  }



  /**
   * [GET] 해시태그별 게시글 조회
   * 예: /api/community/posts/tag/3
   */
  @GetMapping("/tag/{tagId}")
  public ResultData<List<CommunityPostDTO>> getPostsByTag(@PathVariable Long tagId) {
    List<CommunityPostDTO> result = communityPostService.getPostsByTag(tagId);
    return ResultData.success(result.size(), result);
  }



}



