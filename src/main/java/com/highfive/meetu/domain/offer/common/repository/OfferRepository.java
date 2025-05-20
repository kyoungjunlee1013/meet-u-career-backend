package com.highfive.meetu.domain.offer.common.repository;

import com.highfive.meetu.domain.offer.common.entity.Offer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

  // ✅ 받은 제안 개수 반환
  int countByPersonalAccountId(Long personalAccountId); // 이미 존재함 (사용 가능)

  @Query("SELECT o FROM offer o WHERE o.personalAccount.id = :accountId")
  List<Offer> findAllOffersByPersonalAccountId(@Param("accountId") Long accountId);

  @Modifying
  @Query("UPDATE offer o SET o.status = 1 WHERE o.id = :offerId")
  void updateStatusToApproved(@Param("offerId") Long offerId);

  @Modifying
  @Query("UPDATE offer o SET o.status = 2 WHERE o.id = :offerId")
  void rejectOffer(@Param("offerId") Long offerId);

  // ✅ 필요한 경우 status 기준 조건도 줄 수 있음
  @Query("SELECT COUNT(o) FROM offer o WHERE o.personalAccount.id = :accountId AND o.status = 0")
  int countPendingOffersByAccountId(@Param("accountId") Long accountId); // 선택사항
}
