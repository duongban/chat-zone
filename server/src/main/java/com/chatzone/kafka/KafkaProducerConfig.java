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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 *
 * @author duongban
 */
@Configuration
public class KafkaProducerConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConfig.class);
    
    @Value("${kafka.config.producer}")
    private String producerConfigFilePath;
    
    public KafkaTemplate<String, Message> kafkaTemplate() {
        try {
            Properties props = new Properties();
            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream(producerConfigFilePath)) {
                props.load(input);
            }
            ProducerFactory<String, Message> producerFactory
                    = new DefaultKafkaProducerFactory(props);
            return new KafkaTemplate<>(producerFactory);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }
}
