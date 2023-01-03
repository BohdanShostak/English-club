package com.inventorsoft.english;

import com.inventorsoft.english.scheduler.config.SchedulerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(SchedulerProperties.class)
public class EnglishClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishClubApplication.class, args);
	}

}
