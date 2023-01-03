package com.inventorsoft.english.googlestorage.util;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {

    @Bean
    public Storage config() {
        StorageOptions options = StorageOptions.newBuilder()
                .build();
        return options.getService();
    }
}
