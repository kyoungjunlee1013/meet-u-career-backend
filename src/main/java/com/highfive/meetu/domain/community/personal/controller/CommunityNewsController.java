package com.highfive.meetu.domain.community.personal.controller;

import com.highfive.meetu.domain.community.personal.dto.CommunityNewsDTO;
import com.highfive.meetu.domain.community.personal.dto.CommunityNewsRequestDTO;
import com.highfive.meetu.domain.community.personal.service.CommunityNewsService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/community/news")
@RequiredArgsConstructor
public class CommunityNewsController {

  private final CommunityNewsService communityNewsService;

// 태그 id로 해시태그를 받아와서, 해시태그 키워드로 뉴스목록을 받아옴
  @PostMapping()
  public ResultData<List<CommunityNewsDTO>> getNewsByTag(@RequestBody CommunityNewsRequestDTO dto) {

    if (dto.getFrom() == null || dto.getTo() == null) {
      LocalDate now = LocalDate.now();
      dto.setFrom(now.minusDays(30));
      dto.setTo(now);
    }

    List<CommunityNewsDTO> result = communityNewsService.getNewsByTagId(dto.getTagId(), dto.getFrom(), dto.getTo());
    return ResultData.success(result.size(), result);
  }
}
