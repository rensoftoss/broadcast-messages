package com.rensoftoss.broadcastmessages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(value = {
        "classpath:spring-configs/common-config.xml",
        "classpath:spring-configs/message-websocket-config.xml",
        "classpath:spring-configs/rest-to-kafka-config.xml",
        "classpath:spring-configs/kafka-to-db-config.xml"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
