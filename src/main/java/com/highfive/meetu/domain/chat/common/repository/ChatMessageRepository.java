package com.highfive.meetu.domain.chat.common.repository;

import com.highfive.meetu.domain.chat.common.entity.ChatMessage;
import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.user.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom_IdOrderByCreatedAtAsc(Long chatRoomId);

    @Query("""
        SELECT COUNT(m) FROM chatMessage m
        WHERE m.chatRoom IN (
            SELECT cr FROM chatRoom cr
            WHERE cr.businessAccount = :account OR cr.personalAccount = :account
        )
        AND m.sender.id != :accountId
        AND m.isRead = 0
    """)
    long countUnreadMessagesByAccount(@Param("account") Account account, @Param("accountId") Long accountId);
    @Query("""
    SELECT m FROM chatMessage m WHERE m.chatRoom = :room ORDER BY m.createdAt DESC
""")
    List<ChatMessage> findRecentMessagesByRoom(@Param("room") ChatRoom room);

    @Query("""
    SELECT COUNT(m) FROM chatMessage m
    WHERE m.chatRoom = :room AND m.sender <> :me AND m.isRead = 0
""")
    int countUnreadByRoomAndReceiver(@Param("room") ChatRoom room, @Param("me") Account me);

}
