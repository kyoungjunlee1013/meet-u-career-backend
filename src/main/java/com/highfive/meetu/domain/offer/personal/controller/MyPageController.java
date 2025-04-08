package com.highfive.meetu.domain.offer.personal.controller;


import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import com.highfive.meetu.domain.offer.personal.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final OfferService offerService;


  @GetMapping("/my-offers/{accountId}")
  public MyOfferDTO getMyOffersForTest(@PathVariable Long accountId) {
    return offerService.getMyOffers(accountId);
  }

}

