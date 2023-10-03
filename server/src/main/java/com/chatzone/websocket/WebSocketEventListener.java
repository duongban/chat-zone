/*
 * The MIT License
 *
 * Copyright 2023 duongban.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.chatzone.websocket;

import com.chatzone.db.entity.MessageEntity;
import com.chatzone.db.repository.MessageRepository;
import com.chatzone.model.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 *
 * @author duongban
 */
@Component
public class WebSocketEventListener {

    @Autowired
    private MessageRepository repo;

    @Autowired
    private SimpMessagingTemplate template;

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        String sessionId = (String) message.getHeaders().get("simpSessionId");
        if (simpDestination == null
                || !simpDestination.contains("/topic/group/")) {
            return;
        }
        String[] parts = simpDestination.split("/");
        String roomCode = parts[parts.length - 1];
        List<MessageEntity> data = repo.findByRoomCode(roomCode);
        for (MessageEntity entity : data) {
            Message msg = new Message();
            msg.setContent(entity.getContent());
            msg.setSender(entity.getSender());
            msg.setTimestamp(entity.getTimestamp());
            template.convertAndSendToUser(
                    sessionId, "/queue/load-history",
                    msg, createHeaders(sessionId));
        }
    }
}
