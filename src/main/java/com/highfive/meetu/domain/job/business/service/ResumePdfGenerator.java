package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Component
public class ResumePdfGenerator {

    public void generateResumePdf(Resume resume, HttpServletResponse response) {
        try {
            String fileName = URLEncoder.encode(resume.getTitle(), "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");

            OutputStream out = response.getOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== 1. 폰트 설정 =====
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSansKR-Regular.ttf");
            if (fontStream == null) {
                throw new IllegalStateException("폰트 파일을 찾을 수 없습니다: fonts/NotoSansKR-Regular.ttf");
            }
            BaseFont baseFont = BaseFont.createFont(
                "fonts/NotoSansKR-Regular.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
            );
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font sectionFont = new Font(baseFont, 14, Font.BOLD);
            Font contentFont = new Font(baseFont, 12);
            Font headerFont = new Font(baseFont, 12, Font.BOLD, BaseColor.WHITE);

            // ===== 2. 이력서 제목 =====
            document.add(new Paragraph("이력서: " + resume.getTitle(), titleFont));
            document.add(new Paragraph(" "));

            // ===== 3. 프로필 사진 =====
            String profileImageUrl = resume.getProfile().getProfileImageKey();
            if (profileImageUrl != null && !profileImageUrl.isBlank()) {
                try {
                    Image profileImage = Image.getInstance(new URL(profileImageUrl));
                    profileImage.scaleToFit(100, 100);
                    profileImage.setAlignment(Element.ALIGN_RIGHT);
                    document.add(profileImage);
                } catch (Exception e) {
                    log.warn("프로필 이미지 로딩 실패: {}", profileImageUrl);
                }
            }

            // ===== 4. 기본 정보 =====
            document.add(new Paragraph("[기본 정보]", sectionFont));
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10f);
            infoTable.setSpacingAfter(10f);

            infoTable.addCell(createCell("이름", headerFont, BaseColor.GRAY));
            infoTable.addCell(createCell(resume.getProfile().getAccount().getName(), contentFont));

            infoTable.addCell(createCell("연락처", headerFont, BaseColor.GRAY));
            infoTable.addCell(createCell(resume.getProfile().getAccount().getPhone(), contentFont));

            infoTable.addCell(createCell("이메일", headerFont, BaseColor.GRAY));
            infoTable.addCell(createCell(resume.getProfile().getAccount().getEmail(), contentFont));

            document.add(infoTable);

            // ===== 5~10. 이력 섹션별 출력 =====
            addResumeSection(document, resume, "학력", sectionFont, contentFont);
            addResumeSection(document, resume, "경력", sectionFont, contentFont);
            addResumeSection(document, resume, "프로젝트", sectionFont, contentFont);
            addResumeSection(document, resume, "기술 스택", sectionFont, contentFont);
            addResumeSection(document, resume, "언어", sectionFont, contentFont);
            addResumeSection(document, resume, "자격증", sectionFont, contentFont);

            // ===== 11. 자기소개서 =====
            document.add(new Paragraph("[자기소개서]", sectionFont));
            if (resume.getCoverLetter() != null && resume.getCoverLetter().getCoverLetterContentList() != null) {
                for (var cl : resume.getCoverLetter().getCoverLetterContentList()) {
                    document.add(new Paragraph("[" + cl.getSectionTitle() + "]", contentFont));
                    document.add(new Paragraph(cl.getContent(), contentFont));
                    document.add(new Paragraph(" "));
                }
            } else {
                document.add(new Paragraph("자기소개서가 없습니다.", contentFont));
            }

            document.close();
        } catch (Exception e) {
            log.error("PDF 생성 중 오류 발생", e);
        }
    }

    private void addResumeSection(Document document, Resume resume, String sectionTitle, Font sectionFont, Font contentFont) throws DocumentException {
        document.add(new Paragraph("[" + sectionTitle + "]", sectionFont));
        List<?> contents = resume.getResumeContentList().stream()
            .filter(c -> sectionTitle.equals(c.getSectionTitle()))
            .toList();

        if (contents.isEmpty()) {
            document.add(new Paragraph(sectionTitle + " 정보가 없습니다.", contentFont));
            return;
        }

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new int[]{2, 2, 6});

        Font headerFont = new Font(contentFont.getBaseFont(), 12, Font.BOLD, BaseColor.WHITE);
        table.addCell(createCell("제목", headerFont, BaseColor.DARK_GRAY));
        table.addCell(createCell("기관", headerFont, BaseColor.DARK_GRAY));
        table.addCell(createCell("설명", headerFont, BaseColor.DARK_GRAY));

        for (var content : contents) {
            var c = (com.highfive.meetu.domain.resume.common.entity.ResumeContent) content;
            table.addCell(createCell(c.getTitle(), contentFont));
            table.addCell(createCell(c.getOrganization(), contentFont));
            table.addCell(createCell(c.getDescription(), contentFont));
        }

        document.add(table);
    }

    private PdfPCell createCell(String text, Font font) {
        return createCell(text, font, BaseColor.WHITE);
    }

    private PdfPCell createCell(String text, Font font, BaseColor background) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        return cell;
    }
}
