package com.highfive.meetu.domain.chat.personal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatFileService {

    public String uploadFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String newName = UUID.randomUUID() + ext;

        try {
            // ✅ 실행 중인 프로젝트 경로 가져오기
            String basePath = System.getProperty("user.dir");

            // ✅ 프로젝트/uploads/chat 디렉토리로 고정
            Path dirPath = Paths.get(basePath, "uploads", "chat");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath); // 폴더 없으면 만들기
            }

            Path fullPath = dirPath.resolve(newName);
            file.transferTo(fullPath.toFile());

            // 디버깅용 로그
            System.out.println("✅ 저장 위치: " + fullPath.toAbsolutePath());
            System.out.println("✅ 파일 크기: " + file.getSize());

            return "/static/chat/" + newName;

        } catch (IOException e) {
            e.printStackTrace(); // 에러 로그 확인용
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
