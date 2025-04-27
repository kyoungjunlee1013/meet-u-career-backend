package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 프로필 엔티티에 대한 JPA 레파지토리
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    /**
     * 계정 ID로 프로필 조회
     * @param accountId 계정 ID
     * @return 프로필(Optional)
     */
    Optional<Profile> findByAccountId(Long accountId);
}
