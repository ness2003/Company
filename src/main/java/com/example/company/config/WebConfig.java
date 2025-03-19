package com.example.company.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешаем все пути
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:8080") // Разрешаем запросы с этих доменов
                .allowedMethods("*")
                .allowedHeaders("*") // Разрешаем все заголовки
                .allowCredentials(true); // Разрешаем передачу учетных данных (например, cookies)
    }
}
