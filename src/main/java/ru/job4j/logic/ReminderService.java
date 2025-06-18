package ru.job4j.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    @PostConstruct
    public void init() {
        System.out.println("ReminderService init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("ReminderService destroy");
    }
}
