package com.rensoftoss.broadcastmessages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.cors.CorsConfiguration;

@SpringBootApplication
@ImportResource(value = {
        "classpath:spring-configs/common-config.xml",
        "classpath:spring-configs/rest-to-kafka-config.xml",
        "classpath:spring-configs/kafka-to-db-config.xml",
        "classpath:spring-configs/kafka-to-websocket-config.xml"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CorsConfiguration cors() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        return corsConfiguration;
    }
}
