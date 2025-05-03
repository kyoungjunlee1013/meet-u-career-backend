package com.highfive.meetu.domain.user.business.service;

import com.highfive.meetu.domain.user.personal.type.CertificationVerifyResult;
import com.highfive.meetu.infra.smtp.MailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BusinessCertificationService {

  private final MailClient mailClient;
  private final StringRedisTemplate redisTemplate;
  private static final long CERTIFICATION_EXPIRATION_MINUTES = 5;

  // 기업용 (name, email, type)
  public void businessSendCertification(String name, String email, String type) {
    String certificationCode = createCertificationCode();

    String subject = "Meet U 기업회원 가입 인증 메일";
    String htmlContent = buildCertificationEmailContent(name, certificationCode);
    mailClient.send(email, subject, htmlContent, true);

    redisTemplate.opsForValue().set(
        getCertificationKey(type, email),
        certificationCode,
        CERTIFICATION_EXPIRATION_MINUTES,
        TimeUnit.MINUTES
    );
  }

  public CertificationVerifyResult verifyCertification(String email, String inputCode) {
    String key = getCertificationKey("business", email);
    String savedCode = redisTemplate.opsForValue().get(key);

    if (savedCode == null) return CertificationVerifyResult.EXPIRED;
    if (savedCode.equals(inputCode)) {
      redisTemplate.delete(key);
      return CertificationVerifyResult.SUCCESS;
    }
    return CertificationVerifyResult.FAIL;
  }

  private String createCertificationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000);
    return String.valueOf(code);
  }

  private String buildCertificationEmailContent(String name, String code) {
    String maskName = maskName(name);

    return "<div style=\"padding:30px; text-align:center; font-family:'Arial', sans-serif;\">" +
        "<h2 style=\"margin-bottom:20px;\">기업 이메일 인증코드 발급</h2>" +
        "<p style=\"margin-bottom:30px; font-size:14px; color:#555;\">" +
        maskName + "님의 이메일 인증을 위한 인증 코드를 발급하였습니다.</p>" +
        "<div style=\"font-size:36px; font-weight:bold; color:#1842a3;\">" + code + "</div>" +
        "<p style=\"margin-top:30px; font-size:12px; color:#999;\">" +
        "인증 코드를 입력해 주세요.</p>" +
        "</div>";
  }

  private String maskName(String name) {
    if (name == null || name.length() < 2) return name;
    if (name.length() == 2) return name.charAt(0) + "*";
    StringBuilder masked = new StringBuilder();
    masked.append(name.charAt(0));
    for (int i = 1; i < name.length() - 1; i++) masked.append("*");
    masked.append(name.charAt(name.length() - 1));
    return masked.toString();
  }

  private String getCertificationKey(String type, String email) {
    return "certification:" + type + ":" + email;
  }
}
