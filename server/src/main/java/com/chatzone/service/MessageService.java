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
package com.chatzone.service;

import com.chatzone.db.entity.MessageEntity;
import com.chatzone.db.repository.MessageRepository;
import com.chatzone.kafka.KafkaProducerService;
import com.chatzone.model.Message;
import com.chatzone.service.inf.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author duongban
 */
@Service
public class MessageService implements IMessageService {
    
    @Value("${kafka.topic}")
    private String kafkaTopic;
    private final KafkaProducerService kafkaProducer;
    
    @Autowired
    private MessageRepository repo;
    
    @Autowired
    public MessageService(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
    
    @Override
    public void sendMessage(String roomCode, Message msg) {
        kafkaProducer.sendMessage(kafkaTopic, roomCode, msg);
        MessageEntity entity = new MessageEntity();
        entity.setRoomCode(roomCode);
        entity.setSender(msg.getSender());
        entity.setContent(msg.getContent());
        entity.setTimestamp(msg.getTimestamp());
        repo.save(entity);
    }
}
