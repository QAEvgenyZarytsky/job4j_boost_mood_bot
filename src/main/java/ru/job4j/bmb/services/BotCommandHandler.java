package ru.job4j.bmb.services;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        if (message == null || message.getText() == null) {
            return Optional.empty();
        }

        String text = message.getText().trim();
        Long clientId = message.getFrom().getId();

        Optional<User> userOpt = Optional.ofNullable(userRepository.findByClientId(clientId));

        switch (text) {
            case "/start":
                return userOpt.flatMap(value -> handleStartCommand(value.getChatId(), clientId));

            case "/week_mood_log":
                if (userOpt.isPresent()) {
                    return moodService.weekMoodLogCommand(userOpt.get().getChatId(), clientId);
                } else {
                    return Optional.empty();
                }

            case "/month_mood_log":
                if (userOpt.isPresent()) {
                    return moodService.monthMoodLogCommand(userOpt.get().getChatId(), clientId);
                } else {
                    return Optional.empty();
                }

            case "/award":
                if (userOpt.isPresent()) {
                    return moodService.awards(userOpt.get().getChatId(), clientId);
                } else {
                    return Optional.empty();
                }

            default:
                return Optional.empty();
        }
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        Content content = moodService.chooseMood(user, moodId);
        return Optional.ofNullable(content);
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
