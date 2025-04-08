package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityCommentDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityCommentService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommunityCommentController {

  private final CommunityCommentService commentService;

  /**
   * [POST] 댓글 등록
   */
  @PostMapping
  public ResultData<CommunityCommentDTO> create(@RequestBody CommunityCommentDTO dto) {
    CommunityCommentDTO result = commentService.createComment(dto);
    return ResultData.success(1, result);
  }

  /**
   * [GET] 게시글 댓글 목록 조회
   */
  @GetMapping("/{postId}")
  public ResultData<List<CommunityCommentDTO>> getList(@PathVariable Long postId) {
    List<CommunityCommentDTO> result = commentService.getCommentsByPost(postId);
    return ResultData.success(result.size(), result);
  }
}
