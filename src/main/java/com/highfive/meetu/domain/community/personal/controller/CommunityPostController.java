package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityPostDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityPostService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

  private final CommunityPostService communityPostService;

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
  public ResultData<List<CommunityPostDTO>> getAllActivePosts() {
    List<CommunityPostDTO> result = communityPostService.getAllActivePosts();
    return ResultData.success(result.size(), result);
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
}



