package ru.job4j.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    @PostConstruct
    public void init() {
        System.out.println("AchievementService init");
    }

    @PreDestroy
    public void destroy() {

        System.out.println("AchievementService destroy");
    }
}
