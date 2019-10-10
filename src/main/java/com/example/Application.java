package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Основной класс для запуска Spring Application
 */

@EnableAsync
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
    }
}
