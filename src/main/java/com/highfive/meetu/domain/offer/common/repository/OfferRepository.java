package com.highfive.meetu.domain.offer.common.repository;

import com.highfive.meetu.domain.offer.common.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

  int countByPersonalAccountId(Long accountId);

  List<Offer> findByPersonalAccountId(Long accountId);

  @Modifying
  @Query("UPDATE offer o SET o.status = 1 WHERE o.id = :offerId")
  void updateStatusToApproved(@Param("offerId") Long offerId);

  @Modifying
  @Query("UPDATE offer o SET o.status = 2 WHERE o.id = :offerId")
  void rejectOffer(@Param("offerId") Long offerId);
}