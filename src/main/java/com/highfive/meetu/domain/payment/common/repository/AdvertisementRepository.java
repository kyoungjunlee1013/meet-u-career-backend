package com.highfive.meetu.domain.payment.common.repository;

import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

}
