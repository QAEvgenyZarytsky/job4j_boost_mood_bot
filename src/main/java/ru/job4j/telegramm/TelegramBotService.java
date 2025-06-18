package ru.job4j.telegramm;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {

    @PostConstruct
    public void init() {
        System.out.println("TelegramBotService init");
    }

    @PreDestroy
    public void destroy() {

        System.out.println("TelegramBotService destroy!");
    }
}
