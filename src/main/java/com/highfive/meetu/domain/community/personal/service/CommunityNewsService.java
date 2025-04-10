package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import com.highfive.meetu.domain.community.common.repository.CommunityTagRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityNewsDTO;
import com.highfive.meetu.domain.community.personal.dto.CommunityTagDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * NewsAPI 연동 뉴스 조회 서비스
 */
@Service
@RequiredArgsConstructor
public class CommunityNewsService {

  private final CommunityTagRepository communityTagRepository;

  // application.yml 에 정의된 API 키
  @Value("${api.newsapi.key}")
  private String newsApiKey;

  private final RestTemplate restTemplate = new RestTemplate();



  /**
   * tagId를 기반으로 해시태그 이름을 키워드로 변환 → NewsAPI에서 뉴스 검색
   * @param tagId 해시태그 ID
   * @return 뉴스 DTO 리스트 (최대 5개)
   */
  public List<CommunityNewsDTO> getNewsByTagId(Long tagId) {
    // 1. 해시태그 조회
    CommunityTag tag = communityTagRepository.findById(tagId)
        .orElseThrow(() -> new NotFoundException("해시태그 정보를 찾을 수 없습니다."));

    // 2. 태그 이름에 맞는 키워드 리스트 가져오기
    List<String> keywords = CommunityTagDTO.getSearchKeywordsByTag(tag.getName());

    System.out.println(keywords);

    // 3. 뉴스 검색어로 연결된 모든 뉴스 가져오기
    List<CommunityNewsDTO> newsList = new ArrayList<>();

    for (String keyword : keywords) {
      String url = "https://newsapi.org/v2/everything?q=" + UriUtils.encode(keyword, StandardCharsets.UTF_8) +
          "&pageSize=5&sortBy=publishedAt&language=ko&apiKey=" + newsApiKey;

      // 4. API 요청 실행
      ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

      // 5. 실패 시 예외 처리
      if (response.getStatusCode() != HttpStatus.OK) {
        throw new RuntimeException("뉴스 API 요청 실패: " + response.getStatusCode());
      }

      // 6. 결과 파싱
      List<Map<String, Object>> articles = (List<Map<String, Object>>) response.getBody().get("articles");

      // 7. DTO 변환 및 결과 리스트에 추가
      for (Map<String, Object> article : articles) {
        newsList.add(CommunityNewsDTO.builder()
            .title((String) article.get("title"))
            .description((String) article.get("description"))
            .url((String) article.get("url"))
            .publishedAt((String) article.get("publishedAt"))
            .sourceName(((Map<String, String>) article.get("source")).get("name"))
            .build());
      }
    }

    // 8. 최종 뉴스 리스트 반환
    return newsList;
  }
}
