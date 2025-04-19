package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 이메일(아이디) 중복 체크
    boolean existsByEmail(String email);

    // 아이디 로그인용
    Optional<Account> findByUserIdAndAccountType(String userId, int accountType);

    // 소셜 로그인용
    Optional<Account> findByEmailAndAccountType(String email, int accountType);
}
