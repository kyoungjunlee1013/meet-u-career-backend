package com.highfive.meetu.domain.offer.common.repository;

import com.highfive.meetu.domain.offer.common.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
