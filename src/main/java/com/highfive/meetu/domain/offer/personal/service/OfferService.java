package com.highfive.meetu.domain.offer.personal.service;


import com.highfive.meetu.domain.offer.common.entity.Offer;
import com.highfive.meetu.domain.offer.common.repository.OfferRepository;
import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
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

  @Transactional
  public void approveOffer(Long offerId) {
    offerRepository.updateStatusToApproved(offerId);
  }

  @Transactional
  public void rejectOffer(Long offerId) {
    offerRepository.rejectOffer(offerId);
  }
  public MyOfferDTO getMyOffers(Long accountId) {
    List<Offer> offers = offerRepository.findAllOffersByPersonalAccountId(accountId);
    return MyOfferDTO.build(offers);
  }

}