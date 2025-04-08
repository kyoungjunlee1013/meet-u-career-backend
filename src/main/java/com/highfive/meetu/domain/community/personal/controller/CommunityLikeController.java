package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityLikeDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityLikeService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 커뮤니티 좋아요 컨트롤러
 */
@RestController
@RequestMapping("/api/community/likes")
@RequiredArgsConstructor
public class CommunityLikeController {

  private final CommunityLikeService communityLikeService;

  /**
   * [POST] 좋아요 토글
   * @param dto accountId, postId 포함
   * @return 생성된 좋아요 DTO or 해제 메시지
   */
  @PostMapping("/toggle")
  public ResultData<?> toggleLike(@RequestBody CommunityLikeDTO dto) {
    CommunityLikeDTO result = communityLikeService.toggleLike(dto.getAccountId(), dto.getPostId());

    if (result == null) {
      return ResultData.success(0, "좋아요가 해제되었습니다.");
    } else {
      return ResultData.success(1, result);
    }
  }
}
