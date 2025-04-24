package com.highfive.meetu.domain.user.common.repository;

import com.highfive.meetu.domain.user.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  /**
   * ------------------------------------------------------
   * Account 엔티티에 대한 CRUD 및 커스텀 조회 기능 제공
   * ------------------------------------------------------
   */

  /**
   * 아이디 찾기
   * 사용자 이름, 이메일, 상태(ACTIVE)로 계정 조회
   *
   * @param name   사용자 이름
   * @param email  사용자 이메일
   * @param status 계정 상태 (예: ACTIVE = 0)
   * @return 일치하는 Account를 Optional로 반환
   */
    Optional<Account> findByNameAndEmailAndStatus(String name, String email, int status);

  /**
   * 비밀번호 찾기
   * 이메일, 이름, 생년월일, 상태(ACTIVE)로 계정 조회
   *
   * @param email  사용자 이메일
   * @param name   사용자 이름
   * @param userId 사용자 userId
   * @param status 계정 상태 (0: ACTIVE)
   * @return 일치하는 Account가 있으면 Optional 반환
   */
    Optional<Account> findByEmailAndNameAndUserIdAndStatus(
      String email,
      String name,
      String userId,
      Integer status);

  /**
   * 이메일로 계정 단건 조회
   *
   * @param email 사용자 이메일
   * @return Optional<Account>
   */
    Optional<Account> findByEmail(String email);

    /**
     *  아이디 중복 체크
     */
    boolean existsByUserId(String userId);

    /**
     * 이메일 중복 체크
     */
    boolean existsByEmail(String email);

    /**
     * 아이디 로그인용
     */
    Optional<Account> findByUserIdAndAccountType(String userId, int accountType);

    /**
     * 소셜 로그인용
     */
    Optional<Account> findByEmailAndAccountType(String email, int accountType);

    @Query("""
        SELECT COUNT(a.id)
        FROM account a
        WHERE a.createdAt >= :start
    """)
    long countUsersCurrent(@Param("start") LocalDateTime start);

    @Query("""
        SELECT COUNT(a.id)
        FROM account a
        WHERE a.createdAt BETWEEN :start AND :end
    """)
    long countUsersPrevious(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
        SELECT FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m'), COUNT(a.id)
        FROM account a
        WHERE YEAR(a.createdAt) = :year
        GROUP BY FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m')
    """)
    List<Object[]> countUsersByMonth(@Param("year") int year);

    @Query("""
        SELECT a.accountType, COUNT(a.id)
        FROM account a
        GROUP BY a.accountType
    """)
    List<Object[]> countUsersByType();
}
