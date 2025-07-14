package ru.job4j.bmb.component;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.fakedb.AchievementFakeRepository;
import ru.job4j.bmb.repository.fakedb.AwardFakeRepository;

import ru.job4j.bmb.repository.fakedb.MoodLogFakeRepository;

import ru.job4j.bmb.services.UserEvent;
import ru.job4j.bmb.services.fakesv.AchievementFakeService;
import ru.job4j.bmb.services.fakesv.SentContentFake;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {
        AchievementFakeService.class,
        MoodLogFakeRepository.class,
        AchievementFakeRepository.class,
        AwardFakeRepository.class,
        SentContentFake.class
})
public class AchievementServiceTest {
    @Autowired
    private AchievementFakeService achievementFakeService;

    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodLogFakeRepository moodLogFakeRepository;

    @Autowired
    @Qualifier("achievementFakeRepository")
    private AchievementFakeRepository achievementRepository;

    @Autowired
    @Qualifier("awardFakeRepository")
    private AwardFakeRepository awardRepository;

    @Autowired
    @Qualifier("sentContentFake")
    private SentContentFake sentContent;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setChatId(12345L);
    }

    @AfterEach
    void tearDown() {
        moodLogFakeRepository.deleteAll();
        achievementRepository.deleteAll();
        awardRepository.deleteAll();
        sentContent.getSentMessages().clear();
    }

    @Test
    void whenUserHasGoodMoodStreakThenAwardIsGiven() {
        Award award1 = new Award();
        award1.setId(1L);
        award1.setDays(3);
        award1.setTitle("Трёхдневный стрик");

        Award award2 = new Award();
        award2.setId(2L);
        award2.setDays(5);
        award2.setTitle("Пятидневный стрик");

        awardRepository.save(award1);
        awardRepository.save(award2);

        for (int i = 0; i < 3; i++) {
            MoodLog moodLog = new MoodLog();
            moodLog.setUser(testUser);
            moodLog.setMood(new Mood("good", true));
            moodLogFakeRepository.save(moodLog);
        }

        UserEvent event = new UserEvent(this, testUser);

        achievementFakeService.onApplicationEvent(event);

        List<Achievement> userAchievements = achievementRepository.findByUser(testUser);

        assertThat(userAchievements).hasSize(1);
        assertThat(userAchievements.get(0).getAward()).isEqualTo(award1);

        List<Content> sent = sentContent.getSentMessages();
        assertThat(sent).hasSize(1);
        assertThat(sent.get(0).getText()).contains("Трёхдневный стрик");
    }
}
