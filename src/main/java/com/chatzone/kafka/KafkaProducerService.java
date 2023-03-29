/*
 * Copyright 2023 duongban.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chatzone.kafka;

import com.chatzone.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author duongban
 */
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaProducerConfig kafkaConf) {
        this.kafkaTemplate = kafkaConf.kafkaTemplate();
    }

    public void sendMessage(String kafkaTopic, Message message) {
        try {
            System.out.println(kafkaTemplate.send(kafkaTopic, message).get());
        } catch (Exception ex) {

        }
    }

}
