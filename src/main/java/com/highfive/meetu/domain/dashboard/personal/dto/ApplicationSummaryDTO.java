package com.highfive.meetu.domain.dashboard.personal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSummaryDTO {
  private Long waiting;
  private Long passedDocument;
  private Long interview1st;
  private Long finalAccepted;
  private Long rejected;
}

