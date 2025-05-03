package com.highfive.meetu.domain.user.business.controller;

import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.s3.S3Service; // 기존 커뮤니티에서 사용 중인 서비스
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/business")
public class BusinessFileUploadController {

  private final S3Service s3Service; // 커뮤니티에서 사용하는 S3 서비스 주입

  @PostMapping("/upload")
  public ResultData<String> uploadBusinessFile(@RequestParam("file") MultipartFile file) {
    // S3Service의 uploadFile(file, folder) 재사용
    String fileKey = s3Service.uploadFile(file, "business"); // business 폴더로 업로드
    return ResultData.success(1, fileKey);
  }
}
