package com.highfive.meetu.domain.user.admin.controller;

import com.highfive.meetu.domain.user.admin.dto.AdminAccountDTO;
import com.highfive.meetu.domain.user.admin.service.AdminAccountService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/accounts")
@CrossOrigin(origins = "*")
public class AdminAccountController {

  private final AdminAccountService adminAccountService;

  /**
   * 관리자 목록 조회
   */
  @GetMapping
  public ResultData<List<AdminAccountDTO>> getAdmins(@RequestParam(required = false) String keyword) {
    List<AdminAccountDTO> list = adminAccountService.getAdminList(keyword);
    return ResultData.success(list.size(), list);
  }

  /**
   * 관리자 생성
   */
  @PostMapping
  public ResultData<Long> createAdmin(@RequestBody AdminAccountDTO dto) {
    Long id = adminAccountService.createAdmin(dto);
    return ResultData.success(1, id);
  }

  /**
   * 관리자 수정
   */
  @PutMapping
  public ResultData<Long> updateAdmin(@RequestBody AdminAccountDTO dto) {
    Long id = adminAccountService.updateAdmin(dto);
    return ResultData.success(1, id);
  }

  /**
   * 관리자 삭제
   */
  @DeleteMapping("/{id}")
  public ResultData<?> deleteAdmin(@PathVariable Long id) {
    adminAccountService.deleteAdmin(id);
    return ResultData.success(1, null);
  }
}
