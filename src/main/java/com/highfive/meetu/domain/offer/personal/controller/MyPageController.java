package com.highfive.meetu.domain.offer.personal.controller;


import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import com.highfive.meetu.domain.offer.personal.service.OfferService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personal/mypage/offers")
@RequiredArgsConstructor
public class MyPageController {

  private final OfferService offerService;


  @GetMapping("/list/{accountId}")
  public ResultData<MyOfferDTO> getMyOffersForTest(@PathVariable Long accountId) {
    MyOfferDTO dto = offerService.getMyOffers(accountId);
    return ResultData.success(1,dto);
  }

}

