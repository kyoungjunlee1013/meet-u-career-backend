package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.QInterviewReview;
import com.highfive.meetu.domain.application.personal.dto.ApplicationPersonalDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewListDTO;
import com.highfive.meetu.domain.application.common.entity.QApplication;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.company.common.entity.QCompany;
import com.highfive.meetu.domain.job.common.entity.QJobPosting;
import com.highfive.meetu.domain.resume.common.entity.QResume;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationQueryRepositoryImpl implements ApplicationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QApplication qApplication = QApplication.application;
    private final QJobPosting qJobPosting = QJobPosting.jobPosting;
    private final QCompany qCompany = QCompany.company;
    private final QInterviewReview review = QInterviewReview.interviewReview;

  @Override
  public List<ApplicationPersonalDTO> findAllByProfileId(Long profileId) {


    return queryFactory
        .select(Projections.constructor(
            ApplicationPersonalDTO.class,
            // ApplicationPersonalDTO 생성자 매핑 순서대로 기재
            qApplication.id, // applicationId
            qApplication.resume.id, // resumeId
            qApplication.resume.title, // resumeTitle
            qApplication.status, // status
            qApplication.createdAt, // appliedAt
            qJobPosting.id, // jobPostingId
            qJobPosting.title, // jobTitle
            qJobPosting.expirationDate, // expirationDate
            qCompany.name // companyName
        ))
        .from(qApplication)
        .join(qApplication.jobPosting, qJobPosting)
        .join(qJobPosting.company, qCompany)
        .where(qApplication.profile.id.eq(profileId))
        .orderBy(qApplication.id.desc()) // ID 기준 내림차순 or 원하는 정렬
        .fetch();
  }

    @Override
    public List<InterviewListDTO> findInterviewsWithReviewStatusByProfileId(Long profileId) {

        return queryFactory
                .select(
                        Projections.constructor(InterviewListDTO.class,
                                qApplication.id,                                  // interviewId
                                qCompany.name,                                    // companyName
                                qJobPosting.title,                                // position
                                qApplication.createdAt.stringValue().substring(0, 10), // interviewDate
                                review.id.isNotNull(),                            // hasReview
                                qCompany.id,                                      // companyId
                                // qJobPosting.jobCategory.id,                   // jobCategoryId
                                qApplication.id                                   // applicationId
                        )
                )
                .from(qApplication)
                .join(qApplication.jobPosting, qJobPosting)
                .join(qJobPosting.company, qCompany)
                .leftJoin(review).on(qApplication.id.eq(review.application.id))
                .where(qApplication.profile.id.eq(profileId))
                .orderBy(qApplication.createdAt.desc())
                .fetch();
    }
}
