package com.highfive.meetu.domain.dashboard.personal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSummaryDTO {
  private Long passedDocument;
  private Long interview1st;
  private Long finalAccepted;
  private Long rejected;
}
