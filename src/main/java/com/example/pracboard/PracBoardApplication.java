package com.example.pracboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PracBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracBoardApplication.class, args);
    }

}
