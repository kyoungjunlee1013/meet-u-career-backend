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
  private final CommunityCommentService communityCommentService;

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


  /**
   * [GET] 내가 쓴 게시글 댓글 목록 조회
   */
  @GetMapping("/my/{accountId}")
  public ResultData<List<CommunityCommentDTO>> getMyComments(@PathVariable Long accountId) {
    List<CommunityCommentDTO> result = communityCommentService.getMyComments(accountId);
    return ResultData.success(result.size(), result);
  }



  /**
   * [DELETE] 댓글 삭제
   */
  @DeleteMapping("/{commentId}")
  public ResultData<String> deleteComment(@PathVariable Long commentId) {
    communityCommentService.deleteComment(commentId);
    return ResultData.success(1, "댓글이 삭제되었습니다.");
  }


}
