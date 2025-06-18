package ru.job4j.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;


@Service
public class MoodService {

    @PostConstruct
    public void init() {
        System.out.println("MoodService init");
    }

    @PreDestroy
    public  void destroy() {
        System.out.println("MoodService destroy");
    }
}
