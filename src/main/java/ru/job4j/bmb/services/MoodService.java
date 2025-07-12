package ru.job4j.bmb.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodService {
    private final MoodRepository moodRepository;
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final ApplicationEventPublisher publisher;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodRepository moodRepository,
                       MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       ApplicationEventPublisher publisher,
                       AchievementRepository achievementRepository) {
        this.moodRepository = moodRepository;
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.publisher = publisher;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        return moodRepository.findById(moodId)
                .map(mood -> {
                    MoodLog moodLog = new MoodLog();
                    moodLog.setUser(user);
                    moodLog.setMood(mood);
                    moodLog.setCreatedAt(Instant.now().getEpochSecond());
                    moodLogRepository.save(moodLog);
                    publisher.publishEvent(new UserEvent(this, user));
                    return recommendationEngine.recommendFor(user.getChatId(), moodId);
                })
                .orElseGet(() -> {
                    Content errorContent = new Content(user.getChatId());
                    errorContent.setText("Настроение с id " + moodId + " не найдено.");
                    return errorContent;
                });
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        return userRepository.findById(clientId)
                .map(user -> {
                    long oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS).getEpochSecond();
                    List<MoodLog> userMoodWeekAgo = moodLogRepository.findByUserAndCreatedAtAfter(user, oneWeekAgo);

                    String message = formatMoodLogs(userMoodWeekAgo, "Ваши настроения за последнюю неделю");

                    Content content = new Content(chatId);
                    content.setText(message);
                    return content;
                });
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        return userRepository.findById(clientId)
                .map(user -> {
                    long oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS).getEpochSecond();

                    List<MoodLog> logs = moodLogRepository.findByUserAndCreatedAtAfter(user, oneMonthAgo);

                    String message = formatMoodLogs(logs, "Ваши настроения за последний месяц");

                    Content content = new Content(chatId);
                    content.setText(message);
                    return content;
                });
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        return userRepository.findById(clientId)
                .map(user -> {
                    List<Achievement> achievements = achievementRepository.findByUser(user);

                    Content content = new Content(chatId);

                    if (achievements.isEmpty()) {
                        content.setText("У вас пока нет наград. Продолжайте поддерживать хорошее настроение!");
                    } else {
                        String message = achievements.stream()
                                .map(ach -> "- " + ach.getAward() + ": " + ach.getCreateAt())
                                .collect(Collectors.joining("\n", "Ваши награды:\n", ""));
                        content.setText(message);
                    }

                    return content;
                });
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nНет записей о настроении.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }
}