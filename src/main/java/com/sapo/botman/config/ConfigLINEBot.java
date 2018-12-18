package com.sapo.botman.config;

import com.sun.glass.ui.Application;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ConfigLINEBot implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String downloadedContentUri = Application.downloadedContentDir.toUri().toASCIIString();
//        log.info("downloaded Uri: {}", downloadedContentUri);
//        registry.addResourceHandler("/downloaded/**")
//                .addResourceLocations(downloadedContentUri);
    }
}
