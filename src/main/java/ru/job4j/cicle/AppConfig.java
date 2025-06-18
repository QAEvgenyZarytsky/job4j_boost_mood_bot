package ru.job4j.cicle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyBeanClass myBean() {
        return new MyBeanClass();
    }
}
