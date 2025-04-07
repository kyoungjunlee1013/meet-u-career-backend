package com.highfive.meetu.domain.user.personal.controller;

import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/profile")
public class ProfilePersonalController {

    private final S3Service s3Service;

    /**
     * 프로필 이미지 업로드
     * @param file 업로드할 이미지 파일
     * @return S3에 저장된 이미지 URL
     */
    @PostMapping("/upload")
    public ResultData<String> uploadProfileImage(@RequestPart MultipartFile file) {
        String key = s3Service.uploadFile(file, "profile");
        String url = s3Service.generatePresignedUrl(key);
        return ResultData.success(1, url);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) {
        String url = s3Service.generatePresignedUrl(key);

        try {
            URL presignedUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) presignedUrl.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream in = connection.getInputStream()) {
                byte[] fileBytes = in.readAllBytes();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition.attachment().filename(key).build());

                return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            throw new BadRequestException("파일 다운로드 실패: " + e.getMessage());
        }
    }
}
