package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class RemindService {
    private final TelegramSenderComponent telegramSenderComponent;
    private final UserRepository userRepository;

    public RemindService(TelegramSenderComponent telegramSenderComponent, UserRepository userRepository) {
        this.telegramSenderComponent = telegramSenderComponent;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        for (var user : userRepository.findAll()) {
            var message = new SendMessage();
            message.setChatId(user.getChatId());
            message.setText("Ping");
            telegramSenderComponent.send(message);
        }
    }
}
