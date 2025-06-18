package ru.job4j.cicle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyBeanClass {

    private String property;

    @Value("Some Value")
    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean is going through init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean will be destroyed now.");
    }

    public void doSomething() {
        System.out.println("Do somth!");
    }
}