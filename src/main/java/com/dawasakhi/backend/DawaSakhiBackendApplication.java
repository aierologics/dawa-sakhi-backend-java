package com.dawasakhi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration.class
})
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class DawaSakhiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawaSakhiBackendApplication.class, args);
        System.out.println("🚀 DawaSure Backend API Server Started Successfully!");
        System.out.println("📚 API Documentation: http://localhost:8080/api/v1/swagger-ui.html");
        System.out.println("💡 Health Check: http://localhost:8080/api/v1/actuator/health");
    }
}