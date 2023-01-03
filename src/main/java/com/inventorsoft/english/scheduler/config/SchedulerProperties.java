package com.inventorsoft.english.scheduler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "schedule")
@Getter
@Setter
public class SchedulerProperties {
    private String lessonGenerator;
    private Integer intervalDays;
}
