package com.highfive.meetu.domain.chat.personal.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StompPresenceTracker {
    // set을 쓰는 이유: 중복되지 않고 accountid를 저장해놔야하니까.
    private final Set<Long> onlineAccountIds = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() {
        log.info("✅ Presence tracker initialized");
    }

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accountIdHeader = accessor.getFirstNativeHeader("accountId");
        if (accountIdHeader != null) {
            try {
                Long accountId = Long.valueOf(accountIdHeader);
                onlineAccountIds.add(accountId);
                log.info("✅ Account {} connected", accountId);
            } catch (NumberFormatException e) {
                log.warn("Invalid accountId header received: {}", accountIdHeader);
            }
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accountIdHeader = accessor.getFirstNativeHeader("accountId");
        if (accountIdHeader != null) {
            try {
                Long accountId = Long.valueOf(accountIdHeader);
                onlineAccountIds.remove(accountId);
                log.info("❌ Account {} disconnected", accountId);
            } catch (NumberFormatException e) {
                log.warn("Invalid accountId on disconnect: {}", accountIdHeader);
            }
        }
    }

    public boolean isOnline(Long accountId) {
        return onlineAccountIds.contains(accountId);
    }
}
