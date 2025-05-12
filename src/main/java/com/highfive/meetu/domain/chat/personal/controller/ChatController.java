package com.highfive.meetu.domain.chat.personal.controller;

import com.highfive.meetu.domain.chat.personal.dto.ChatMessageDTO;
import com.highfive.meetu.domain.chat.personal.dto.ChatRoomSummaryDTO;
import com.highfive.meetu.domain.chat.personal.service.ChatService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    /**
     * STOMP 실시간 채팅 메시지 수신
     */
    @MessageMapping("/chat/message")
    public void send(@Payload ChatMessageDTO chatMessage) {
        try {
            System.out.println("🔥 수신된 메시지: " + chatMessage); // ✅ 로그 찍기

            var saved = chatService.save(chatMessage);
            chatMessage.setId(saved.getId());
            chatMessage.setCreatedAt(saved.getCreatedAt());
            chatMessage.setIsRead(saved.getIsRead());

            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChatRoomId(), chatMessage);
        } catch (Exception e) {
            System.err.println("❌ 채팅 저장 또는 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 채팅방의 메시지 목록 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResultData<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        var messages = chatService.getMessages(roomId);
        return ResultData.success(messages.size(), messages);
    }

    /**
     * 로그인한 사용자가 채팅방의 메시지를 읽음 처리
     */
    @PostMapping("/rooms/{roomId}/read")
    public ResultData<?> markAsRead(@PathVariable Long roomId) {
        Long readerId = SecurityUtil.getAccountId();
        chatService.markMessagesAsRead(roomId, readerId);
        return ResultData.success(1, null);
    }

    /**
     * 로그인한 사용자의 전체 안읽은 메시지 수
     */
    @GetMapping("/unread-count")
    public ResultData<Long> getUnreadCount() {
        Long accountId = SecurityUtil.getAccountId();
        long count = chatService.getUnreadCountForUser(accountId);
        return ResultData.success(1, count);
    }

    /**
     * 로그인한 사용자의 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResultData<List<ChatRoomSummaryDTO>> getMyChatRooms() {
        Long accountId = SecurityUtil.getAccountId();
        List<ChatRoomSummaryDTO> rooms = chatService.getRoomsForUser(accountId);
        return ResultData.success(rooms.size(), rooms);
    }
}
