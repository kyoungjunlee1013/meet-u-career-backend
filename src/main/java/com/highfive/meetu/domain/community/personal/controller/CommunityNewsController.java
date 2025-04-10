package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityNewsDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityNewsService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 뉴스 API 연동용 커뮤니티 뉴스 컨트롤러
 */
@RestController
@RequestMapping("/api/community/news")
@RequiredArgsConstructor
public class CommunityNewsController {

  private final CommunityNewsService communityNewsService;

  /**
   * [GET] 해시태그 기반 뉴스 검색
   * @param tagId 해시태그 ID
   * @return 뉴스 목록 (최신순, 최대 5개)
   */
  @GetMapping("/{tagId}")
  public ResultData<List<CommunityNewsDTO>> getNewsByTag(@PathVariable Long tagId) {

    List<CommunityNewsDTO> result = communityNewsService.getNewsByTagId(tagId);
    return ResultData.success(result.size(), result);
  }
}
