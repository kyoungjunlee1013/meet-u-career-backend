package com.highfive.meetu.domain.offer.personal.controller;


import com.highfive.meetu.domain.offer.personal.dto.MyOfferDTO;
import com.highfive.meetu.domain.offer.personal.service.OfferService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal/mypage/offers")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class OfferController {

  private final OfferService offerService;


  @GetMapping("/list/review/{accountId}")
  public ResultData<MyOfferDTO> getMyOffersForTest(@PathVariable Long accountId) {
    MyOfferDTO dto = offerService.getMyOffers(accountId);
    return ResultData.success(1, dto);
  }

  @PostMapping("/{offerId}/approve")
  public ResultData<MyOfferDTO> approveOffer(@PathVariable Long offerId) {
    offerService.approveOffer(offerId);
    System.out.println("✅ Approve 호출됨");
    return ResultData.success(1, null); // 응답 컨벤션에 따라 null 본문 반환
  }

  @PostMapping("/{offerId}/reject")
  public ResultData<MyOfferDTO> rejectOffer(@PathVariable Long offerId) {
    offerService.rejectOffer(offerId);
    return ResultData.success(1, null);
  }
}