package com.highfive.meetu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MeetUBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetUBackendApplication.class, args);
    }

}
