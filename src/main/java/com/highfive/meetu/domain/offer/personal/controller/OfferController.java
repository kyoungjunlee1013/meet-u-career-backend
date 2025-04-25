package com.highfive.meetu.domain.offer.personal.controller;


import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import com.highfive.meetu.domain.offer.personal.service.OfferService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal/mypage/offers")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class OfferController {

  private final OfferService offerService;


  @GetMapping("/list/review")
  public ResultData<MyOfferDTO> getMyOffersForTest() {
    Long AccountId = SecurityUtil.getAccountId();
    MyOfferDTO dto = offerService.getMyOffers(AccountId);
    return ResultData.success(1, dto);
  }

  @PostMapping("/approve")
  public ResultData<MyOfferDTO> approveOffer() {
    Long OfferId = SecurityUtil.getAccountId();
    offerService.approveOffer(OfferId);
    System.out.println("✅ Approve 호출됨");
    return ResultData.success(1, null); // 응답 컨벤션에 따라 null 본문 반환
  }

  @PostMapping("/reject")
  public ResultData<MyOfferDTO> rejectOffer() {
    Long OfferId = SecurityUtil.getAccountId();
    offerService.rejectOffer(OfferId);
    return ResultData.success(1, null);
  }

  @GetMapping("/list/all")
  public ResultData<MyOfferDTO> getAllOffers() {
    Long AccountId = SecurityUtil.getAccountId();
    MyOfferDTO dto = offerService.getMyOffers(AccountId); // status 관계없이 전부 조회
    return ResultData.success(1, dto);
  }
}