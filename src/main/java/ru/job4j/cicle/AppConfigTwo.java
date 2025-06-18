package ru.job4j.cicle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfigTwo {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.timeout}")
    private int timeout;

    @Value("${app.nonexistent:default_value}")
    private String someValue;

    public void printConfig() {

        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
        System.out.println("App URL: " + appUrl);
        System.out.println("Timeout: " + timeout);
        System.out.println("SomeValue: " + someValue);
    }
}