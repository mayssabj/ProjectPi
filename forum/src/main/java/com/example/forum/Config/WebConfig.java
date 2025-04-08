package com.example.forum.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/forum-uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(3600);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*registry.addMapping("/forum/forum-uploads/**")
                .allowedOrigins("*")
                .allowedMethods("GET");*/
        registry.addMapping("/**") // Enable CORS for all paths
                .allowedOrigins("http://localhost:4200") // Allow frontend to access
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With") // Allowed headers
                .allowCredentials(true); // Allow credentials (cookies, etc.)
    }

}
