package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final UserRepository userRepository;
    private final TgUI tgUI;

    public ReminderService(SentContent sentContent,
                           MoodLogRepository moodLogRepository,
                           UserRepository userRepository,
                           TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUsers() {
        var startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        var endOfDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        var allUsers = userRepository.findAll();
        for (var user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay, allUsers)) {
            var content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }
}
