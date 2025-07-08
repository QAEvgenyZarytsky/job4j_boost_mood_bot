package ru.job4j.bmb.services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramSenderComponent {

    private final TelegramLongPollingBot bot;

    public TelegramSenderComponent(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void send(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}