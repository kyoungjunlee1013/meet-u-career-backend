package com.highfive.meetu.domain.chat.common.repository;

import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.user.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByBusinessAccountOrPersonalAccount(Account b, Account p);


}
