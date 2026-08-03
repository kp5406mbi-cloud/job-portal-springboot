package com.jobportal;   // Change this to match the folder

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbInfo {

    @Value("${spring.datasource.url}")
    private String url;

    @PostConstruct
    public void init() {
        System.out.println("=================================");
        System.out.println("DATABASE URL = " + url);
        System.out.println("=================================");
    }
}