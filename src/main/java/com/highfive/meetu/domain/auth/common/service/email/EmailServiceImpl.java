package com.highfive.meetu.domain.auth.common.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * JavaMailSender를 이용한 EmailService 구현체
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  // application.yml에 설정된 발신자 이메일 주소
  @Value("${spring.mail.username}")
  private String fromAddress;

  @Override
  public void sendPasswordResetCode(String toEmail, String code) {
    // 이메일 제목과 본문을 작성
    String subject = "[MeetU] 비밀번호 재설정 인증 코드 안내";
    String text = String.format(
        "안녕하세요,\n\n" +
            "비밀번호 재설정을 위한 인증 코드는 다음과 같습니다:\n\n" +
            "    %s\n\n" +
            "이 코드는 10분간 유효합니다.\n\n" +
            "감사합니다.\nMeetU 드림",
        code
    );

    // SimpleMailMessage 생성 및 전송
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromAddress);   // 발신자
    message.setTo(toEmail);         // 수신자
    message.setSubject(subject);    // 제목
    message.setText(text);          // 본문

    mailSender.send(message);
  }
}
