package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


  /** ------------------------------------------------------
   * Account 엔티티에 대한 CRUD 및 커스텀 조회 기능 제공
   * ------------------------------------------------------
   */
  /**
   * 이메일, 이름, 생년월일, 상태(ACTIVE)로 계정 조회
   *
   * @param email    사용자 이메일
   * @param name     사용자 이름
   * @param birthday 사용자 생년월일
   * @param status   계정 상태 (0: ACTIVE)
   * @return 일치하는 Account가 있으면 Optional 반환
   */
  Optional<Account> findByEmailAndNameAndBirthdayAndStatus(
      String email,
      String name,
      LocalDate birthday,
      Integer status
  );

  /**
   * 이메일로 계정 단건 조회
   *
   * @param email 사용자 이메일
   * @return Optional<Account>
   */
  Optional<Account> findByEmail(String email);
}
