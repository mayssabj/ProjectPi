package com.example.forum.Service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@ConfigurationProperties(prefix = "file")
@Configuration
@ConfigurationProperties(prefix ="file" )
public class FileStorageProperties {
    private String uploadDir;

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }
}