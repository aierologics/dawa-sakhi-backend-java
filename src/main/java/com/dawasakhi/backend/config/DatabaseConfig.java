package com.dawasakhi.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    // Database configuration beans can be added here if needed
    // RedisTemplate bean is already defined in BeanConfig
}