package com.highfive.meetu.domain.coverletter.common.repository;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {

    List<CoverLetter> findByProfileIdAndStatusOrderByUpdatedAtDesc(Long profileId, Integer status);

    // Status 상수 정의를 추가 (CoverLetter.Status 사용 시 필요)
    class Status {
        public static final int ACTIVE = 0;   // 정상
        public static final int DRAFT = 1;    // 임시 저장
        public static final int DELETED = 2;  // 삭제됨
    }

}
