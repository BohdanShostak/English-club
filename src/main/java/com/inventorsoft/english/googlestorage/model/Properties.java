package com.inventorsoft.english.googlestorage.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcp.storage.bucket")
@Data
public class Properties {

    private String homeWork;

    private String images;
}
