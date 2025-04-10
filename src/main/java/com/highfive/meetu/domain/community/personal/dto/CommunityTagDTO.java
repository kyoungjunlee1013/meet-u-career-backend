package com.highfive.meetu.domain.community.personal.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTagDTO {

  private Long id; // 태그 ID
  private String name; // 태그 이름
  private Integer status; // 태그 상태 (0: 사용 중, 1: 비활성화)
  private LocalDateTime createdAt; // 태그 생성일
  private LocalDateTime updatedAt; // 태그 수정일


  // 태그 이름별로 검색 키워드를 반환하는 메서드
  public static List<String> getSearchKeywordsByTag(String tagName) {
    switch (tagName) {
      case "면접":
        return List.of("면접", "면접 준비");
      case "이직":
        return List.of("이직", "구직");
      case "연봉":
        return List.of("연봉", "연봉 협상");
      case "취업":
        return List.of("취업", "취업 전략");
      case "자기소개서":
        return List.of("자기소개서", "자기소개서 작성법");
      case "커리어":
        return List.of("커리어", "경력 개발");
      case "자격증":
        return List.of("자격증", "국가 자격증");
      default:
        return List.of(tagName);  // 기본적으로 태그명 자체를 사용
    }
  }
}