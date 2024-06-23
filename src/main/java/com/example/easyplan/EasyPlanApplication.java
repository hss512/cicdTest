package com.example.easyplan;

import com.example.easyplan.domain.entity.file.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableJpaAuditing
public class EasyPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyPlanApplication.class, args);
    }

}
