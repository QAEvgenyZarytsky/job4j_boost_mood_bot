package ru.job4j.bmb.services;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    private final MoodLogRepository moodLogRepository;
    private final AchievementRepository achievementRepository;
    private final SentContent sentContent;
    private final AwardRepository awardRepository;

    public AchievementService(MoodLogRepository moodLogRepository,
                              AchievementRepository achievementRepository,
                              SentContent sentContent,
                              AwardRepository awardRepository) {
        this.moodLogRepository = moodLogRepository;
        this.achievementRepository = achievementRepository;
        this.sentContent = sentContent;
        this.awardRepository = awardRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        User user = event.getUser();

        Set<Award> awardsOwned = achievementRepository.findByUser(user).stream()
                .map(Achievement::getAward)
                .collect(Collectors.toSet());

        awardRepository.findAll().stream()
                .filter(award -> award.getDays() == getGoodMoodStreak(user))
                .filter(award -> !awardsOwned.contains(award))
                .forEach(award -> giveAward(user, award));

    }

    private int getGoodMoodStreak(User user) {
        List<MoodLog> listMoodLogByUser = moodLogRepository.findByUserId(user.getId());
        Collections.reverse(listMoodLogByUser);
        int countStrike = 0;
        for (MoodLog moodLog : listMoodLogByUser) {
            if (moodLog.getMood().isGood()) {
                countStrike++;
            } else {
                break;
            }
        }
        return countStrike;
    }

    private void giveAward(User user, Award award) {
        Achievement achievement = new Achievement();
        achievement.setUser(user);
        achievement.setAward(award);
        achievement.setCreateAt(Instant.now().getEpochSecond());

        achievementRepository.save(achievement);

        Content content = new Content(user.getChatId());
        content.setText("Поздравляем! Вы получили награду: " + award.getTitle());
        sentContent.sent(content);
    }
}
