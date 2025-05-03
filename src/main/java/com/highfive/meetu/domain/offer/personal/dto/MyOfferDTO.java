package com.highfive.meetu.domain.offer.personal.dto;

import com.highfive.meetu.domain.offer.common.entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyOfferDTO {

  private List<OfferDetail> offerList;

  public static MyOfferDTO build(List<Offer> offers) {
    List<OfferDetail> list = offers.stream()
        .map(OfferDetail::from)
        .collect(Collectors.toList());
    return new MyOfferDTO(list);
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OfferDetail {
    private int status;
    private Long id; // Offer ID
    private String companyName;
    private LocalDateTime offerDate;
    private String message;
    private String location;

    public static OfferDetail from(Offer offer) {
      return new OfferDetail(
          offer.getStatus(),
          offer.getId(),
          offer.getCompany().getName(),
          offer.getCreatedAt(),
          offer.getMessage(),
          offer.getCompany().getAddress()
      );
    }
  }
}
