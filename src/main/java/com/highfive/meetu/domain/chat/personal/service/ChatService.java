package com.highfive.meetu.domain.chat.personal.service;

import com.highfive.meetu.domain.chat.common.entity.ChatMessage;
import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.chat.common.repository.ChatMessageRepository;
import com.highfive.meetu.domain.chat.common.repository.ChatRoomRepository;
import com.highfive.meetu.domain.chat.personal.dto.ChatMessageDTO;
import com.highfive.meetu.domain.chat.personal.dto.ChatRoomSummaryDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    public ChatMessage save(ChatMessageDTO dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                .orElseThrow(() -> new NotFoundException("채팅방을 찾을 수 없습니다."));
        Account sender = accountRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new NotFoundException("보낸 사용자를 찾을 수 없습니다."));
        ChatMessage message = dto.toEntity(chatRoom, sender);
        return chatMessageRepository.save(message);
    }

    public List<ChatMessageDTO> getMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(chatRoomId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void markMessagesAsRead(Long chatRoomId, Long readerId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(chatRoomId);
        for (ChatMessage msg : messages) {
            if (!msg.getSender().getId().equals(readerId)) {
                msg.setIsRead(ChatMessage.ReadStatus.READ);
            }
        }
        chatMessageRepository.saveAll(messages);
    }

    public long getUnreadCountForUser(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("사용자 정보를 찾을 수 없습니다."));
        return chatMessageRepository.countUnreadMessagesByAccount(account, accountId);
    }

    public List<ChatRoomSummaryDTO> getRoomsForUser(Long accountId) {
        Account me = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("사용자 정보 없음"));
        List<ChatRoom> rooms = chatRoomRepository.findByBusinessAccountOrPersonalAccount(me, me);
        return rooms.stream().map(room -> {
            List<ChatMessage> messages = chatMessageRepository.findRecentMessagesByRoom(room);
            ChatMessage lastMessage = messages.isEmpty() ? null : messages.get(0);
            int unread = chatMessageRepository.countUnreadByRoomAndReceiver(room, me);

            Account opponent = room.getBusinessAccount().equals(me)
                    ? room.getPersonalAccount()
                    : room.getBusinessAccount();

            Profile profile = profileRepository.findByAccount(opponent).orElse(null);
            String avatar = (profile != null) ? profile.getProfileImageKey() : null;

            return ChatRoomSummaryDTO.of(room, me, lastMessage, unread, avatar);
        }).collect(Collectors.toList());
    }

    private ChatMessageDTO toDTO(ChatMessage msg) {
        return ChatMessageDTO.builder()
                .id(msg.getId())
                .chatRoomId(msg.getChatRoom().getId())
                .senderId(msg.getSender().getId())
                .senderName(msg.getSender().getName())
                .senderType(msg.getSenderType())
                .message(msg.getMessage())
                .isRead(msg.getIsRead())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}
