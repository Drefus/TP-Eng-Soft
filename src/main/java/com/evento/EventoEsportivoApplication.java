package com.evento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventoEsportivoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventoEsportivoApplication.class, args);
    }
}
