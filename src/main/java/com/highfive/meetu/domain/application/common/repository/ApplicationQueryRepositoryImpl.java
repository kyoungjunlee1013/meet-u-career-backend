package com.highfive.meetu.domain.application.common.repository;

import com.highfive.meetu.domain.application.common.entity.QInterviewReview;
import com.highfive.meetu.domain.application.personal.dto.InterviewListDTO;
import com.highfive.meetu.domain.application.common.entity.QApplication;
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

  @Override
  public List<InterviewListDTO> findInterviewsWithReviewStatusByProfileId(Long profileId) {
    QApplication application = QApplication.application;
    QJobPosting jobPosting = QJobPosting.jobPosting;
    QCompany company = QCompany.company;
    QInterviewReview review = QInterviewReview.interviewReview;

    return queryFactory
        .select(
            Projections.constructor(InterviewListDTO.class,
                application.id,                              // interviewId
                company.name,                                // companyName
                jobPosting.title,                            // position
                application.createdAt.stringValue().substring(0, 10), // interviewDate
                review.id.isNotNull(),                       // hasReview

                company.id,                                   // companyId
                // jobPosting.jobCategory.id,                    // jobCategoryId
                application.id                                // applicationId
            )

        )
        .from(application)
        .join(application.jobPosting, jobPosting)
        .join(jobPosting.company, company)
        .leftJoin(review).on(application.id.eq(review.application.id))
        .where(application.profile.id.eq(profileId))
        .orderBy(application.createdAt.desc())
        .fetch();
  }

}
