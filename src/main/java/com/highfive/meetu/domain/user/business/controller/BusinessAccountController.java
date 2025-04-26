package com.highfive.meetu.domain.user.business.controller;

import com.highfive.meetu.domain.user.business.dto.BusinessEmailRequestDTO;
import com.highfive.meetu.domain.user.business.dto.BusinessSignUpRequestDTO;
import com.highfive.meetu.domain.user.business.service.BusinessAccountService;
import com.highfive.meetu.domain.user.personal.dto.CertificationRequestDTO;
import com.highfive.meetu.domain.user.personal.dto.CertificationVerifyRequestDTO;
import com.highfive.meetu.domain.user.personal.dto.PersonalSignUpRequestDTO;
import com.highfive.meetu.domain.user.personal.service.CertificationService;
import com.highfive.meetu.domain.user.personal.type.CertificationVerifyResult;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/business")
public class BusinessAccountController {
    private final BusinessAccountService accountService;
  private final CertificationService certificationService;

    // 이메일(아이디) 중복 체크
    @PostMapping("/check/email")
    public ResultData<Boolean> findByEmail(@RequestBody BusinessEmailRequestDTO dto) {
        boolean result = accountService.findByEmail(dto.getEmail());
        return ResultData.success(result ? 1 : 0, result);
    }

    // 기업회원 회원가입
    @PostMapping("/signup")
    public ResultData<BusinessSignUpRequestDTO> signupBusiness(@RequestBody BusinessSignUpRequestDTO dto) {
        BusinessSignUpRequestDTO result = accountService.save(dto);
        return ResultData.success((result != null) ? 1 : 0, result);
    }


  // 이메일 인증 요청
  @PostMapping("/certification")
  public ResultData<?> sendCertificationEmail(@RequestBody CertificationRequestDTO request) {
    certificationService.sendCertification(request.getName(), request.getEmail());
    return ResultData.success(1, "인증 코드를 발송했습니다.");
  }

  // 이메일 인증 검증
  @PostMapping("/certification/verify")
  public ResultData<?> verifyCertification(@RequestBody CertificationVerifyRequestDTO request) {
    CertificationVerifyResult result = certificationService.verifyCertification(
        request.getEmail(),
        request.getCode()
    );

    if (result == CertificationVerifyResult.SUCCESS) {
      return ResultData.success(1, "인증 성공");
    } else if (result == CertificationVerifyResult.EXPIRED) {
      return ResultData.fail("인증 시간이 만료되었습니다. 다시 요청해 주세요.");
    } else {
      return ResultData.fail("인증 코드가 일치하지 않습니다.");
    }
  }
}
