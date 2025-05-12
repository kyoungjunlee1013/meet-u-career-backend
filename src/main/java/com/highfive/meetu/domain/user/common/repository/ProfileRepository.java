package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 프로필 엔티티에 대한 JPA 레파지토리
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Optional<Profile> findByAccountId(Long profileId);

  Optional<Profile> findByAccount(Account account);
}
