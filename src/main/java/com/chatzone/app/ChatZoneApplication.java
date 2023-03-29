package com.chatzone.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chatzone.*"})
public class ChatZoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatZoneApplication.class, args);
    }

}
