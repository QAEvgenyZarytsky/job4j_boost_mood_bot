package ru.job4j.bmb.condition;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Conditional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Conditional(FakeModeCondition.class)
public class FakeTelegramBot extends TelegramLongPollingBot implements SentContent {
    @Override
    public String getBotUsername() {
        return "fake_bot";
    }

    @Override
    public String getBotToken() {
        return "none";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.printf("[FAKE BOT] Received message: %s%n", update.getMessage().getText());
            String chatId = update.getMessage().getChatId().toString();
            sendMessage(chatId, "Fake response to: " + update.getMessage().getText());
        }
    }

    @Override
    public void sendMessage(String chatId, String text) {
        System.out.printf("[FAKE BOT] Would send to chat %s: %s%n", chatId, text);
    }
}