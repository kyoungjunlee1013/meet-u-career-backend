package com.highfive.meetu.domain.payment.business.controller;

import com.highfive.meetu.domain.payment.business.dto.AdvertisementRegisterRequest;
import com.highfive.meetu.domain.payment.business.service.AdvertisementBusinessService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business/advertisement")
public class AdvertisementBusinessController {

    private final AdvertisementBusinessService advertisementBusinessService;

    @PostMapping("/register")
    public ResultData<Long> registerAdWithPayment(@RequestBody AdvertisementRegisterRequest request) {
        //Long companyId = SecurityUtil.getCompanyId();
        Long companyId = 1L;
        return advertisementBusinessService.registerAdWithPayment(companyId, request);
    }


}
