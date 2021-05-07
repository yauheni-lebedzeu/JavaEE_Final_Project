package com.gmail.yauheniylebedzeu.repository.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.gmail.yauheniylebedzeu.repository.model")
public class DatabaseConfig {
}
