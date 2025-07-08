package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Mood;
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

@Service
public class MoodService {
    private final MoodRepository moodRepository;
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodRepository moodRepository,
                       MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodRepository = moodRepository;
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isEmpty()) {
            Content errorContent = new Content(user.getChatId());
            errorContent.setText("Настроение с id " + moodId + " не найдено.");
            return errorContent;
        }
        Mood mood = moodOpt.get();

        MoodLog moodLog = new MoodLog();
        moodLog.setUser(user);
        moodLog.setMood(mood);
        moodLog.setCreatedAt(Instant.now().getEpochSecond());
        moodLogRepository.save(moodLog);

        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var userOpt = userRepository.findById(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();

        long oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS).getEpochSecond();

        List<MoodLog> userMoodWeekAgo = moodLogRepository.findByUserAndCreatedAtAfter(user, oneWeekAgo);

        String message = formatMoodLogs(userMoodWeekAgo, "Ваши настроения за последнюю неделю");

        Content content = new Content(chatId);
        content.setText(message);
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var userOpt = userRepository.findById(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();

        long oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS).getEpochSecond();

        List<MoodLog> logs = moodLogRepository.findByUserAndCreatedAtAfter(user, oneMonthAgo);

        String message = formatMoodLogs(logs, "Ваши настроения за последний месяц");

        Content content = new Content(chatId);
        content.setText(message);
        return Optional.of(content);
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var userOpt = userRepository.findById(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        List<Achievement> achievements = achievementRepository.findByUser(user);

        if (achievements.isEmpty()) {
            Content content = new Content(chatId);
            content.setText("У вас пока нет наград. Продолжайте поддерживать хорошее настроение!");
            return Optional.of(content);
        }

        StringBuilder sb = new StringBuilder("Ваши награды:\n");
        for (Achievement ach : achievements) {
            sb.append("- ").append(ach.getAward()).append(": ").append(ach.getCreateAt()).append("\n");
        }

        Content content = new Content(chatId);
        content.setText(sb.toString());
        return Optional.of(content);
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