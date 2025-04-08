package com.highfive.meetu.domain.offer.personal.service;


import com.highfive.meetu.domain.offer.common.entity.Offer;
import com.highfive.meetu.domain.offer.common.repository.OfferRepository;
import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

  private final OfferRepository offerRepository;

  /**
   * 개인 계정 기준으로 받은 오퍼 목록을 조회하여 DTO로 반환합니다.
   */
  public MyOfferDTO getMyOffers(Long personalAccountId) {
    List<Offer> offers = offerRepository.findByPersonalAccountId(personalAccountId);
    return MyOfferDTO.build(offers);
  }
}