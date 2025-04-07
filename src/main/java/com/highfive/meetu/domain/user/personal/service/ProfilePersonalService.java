package com.highfive.meetu.domain.user.personal.service;

import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProfilePersonalService {

    private final S3Service s3Service;

    public void uploadProfileImage(MultipartFile file) {
        String url = s3Service.uploadFile(file, "profile");
        // 이후 DB 저장 로직 수행


    }
}
