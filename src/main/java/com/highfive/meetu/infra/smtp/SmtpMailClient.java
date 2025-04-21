package com.highfive.meetu.infra.smtp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpMailClient implements MailClient {

  private final JavaMailSender mailSender;

  // application.yml 의 spring.mail.username
  @Value("${spring.mail.username}")
  private String from;

  @Override
  public void send(String to, String subject, String body) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(body);
    mailSender.send(msg);
  }
}
