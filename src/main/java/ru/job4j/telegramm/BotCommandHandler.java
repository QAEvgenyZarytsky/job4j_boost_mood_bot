package ru.job4j.telegramm;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class BotCommandHandler {
    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler destroy");
    }

}
