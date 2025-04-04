package com.highfive.meetu.domain.chat.common.repository;

import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
