package com.rensoftoss.broadcastmessages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.cors.CorsConfiguration;

@SpringBootApplication
@ImportResource(value = {
        "classpath:spring-configs/common-config.xml",
        "classpath:spring-configs/producer-config.xml",
        "classpath:spring-configs/websocket-config.xml",
        "classpath:spring-configs/consumer-config.xml",
        "classpath:spring-configs/consumer-db-config.xml",
        "classpath:spring-configs/consumer-ws-config.xml"
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
