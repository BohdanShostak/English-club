package com.inventorsoft.english.general.config;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DateGenerator {

    public LocalDate generateLocalDate() {
        return LocalDate.now();
    }

    public LocalDateTime generateLocalDateTime() {
        return LocalDateTime.now();
    }
}
