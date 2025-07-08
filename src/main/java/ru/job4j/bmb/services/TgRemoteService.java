package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final KeyboardBuilderComponent keyboardBuilder;
    private final TelegramSenderComponent telegramSender;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository,
                           KeyboardBuilderComponent keyboardBuilder,
                           TelegramSenderComponent telegramSender) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.keyboardBuilder = keyboardBuilder;
        this.telegramSender = telegramSender;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.save(user);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Как настроение сегодня?");
                sendMessage.setReplyMarkup(keyboardBuilder.buildMoodKeyboard());

                telegramSender.send(sendMessage);
            }
        }
    }
}