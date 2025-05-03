package com.highfive.meetu.domain.user.admin.service;

import com.highfive.meetu.domain.user.admin.dto.AdminAccountDTO;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.domain.user.common.repository.AdminRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 관리자 목록 조회
   */
  public List<AdminAccountDTO> getAdminList(String keyword) {
    List<Admin> admins = (keyword == null || keyword.trim().isEmpty())
        ? adminRepository.findAll()
        : adminRepository.findByNameContainingOrEmailContaining(keyword, keyword);
    return admins.stream()
        .map(AdminAccountDTO::build)
        .collect(Collectors.toList());
  }

  /**
   * 관리자 생성
   */
  @Transactional
  public Long createAdmin(AdminAccountDTO dto) {
    Admin admin = Admin.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(dto.getRole())
        .createdAt(LocalDateTime.now())
        .build();

    return adminRepository.save(admin).getId();
  }

  /**
   * 관리자 수정
   */
  @Transactional
  public Long updateAdmin(AdminAccountDTO dto) {
    Admin admin = adminRepository.findById(dto.getId())
        .orElseThrow(() -> new NotFoundException("관리자를 찾을 수 없습니다."));

    admin.setName(dto.getName());
    admin.setEmail(dto.getEmail());
    admin.setRole(dto.getRole());

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      admin.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    return adminRepository.save(admin).getId(); // 🔥 반드시 save()로 영속성 반영
  }

  /**
   * 관리자 삭제
   */
  @Transactional
  public void deleteAdmin(Long id) {
    if (!adminRepository.existsById(id)) {
      throw new NotFoundException("관리자를 찾을 수 없습니다.");
    }
    adminRepository.deleteById(id);
  }
}
