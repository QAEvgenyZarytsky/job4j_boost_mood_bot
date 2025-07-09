package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.fakedb.MoodFakeRepository;
import ru.job4j.bmb.repository.fakedb.MoodLogFakeRepository;
import ru.job4j.bmb.repository.fakedb.UserFakeRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderServiceTest {
    @Test
    public void whenMoodGood() {
        UserFakeRepository userRepository = new UserFakeRepository();

        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setClientId(100L);
        fakeUser.setChatId(12345L);
        userRepository.addUser(fakeUser);

        var result = new ArrayList<Content>();

        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };
        var moodRepository = new MoodFakeRepository();
        moodRepository.save(new Mood("Good", true));


        var moodLogRepository = new MoodLogFakeRepository();
        var user = new User();
        user.setChatId(100);

        var moodLog = new MoodLog();
        moodLog.setUser(user);
        var tenDaysAgo = LocalDate.now()
                .minusDays(10)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        moodLog.setCreatedAt(tenDaysAgo);
        moodLogRepository.save(moodLog);

        var tgUI = new TgUI(moodRepository);
        new ReminderService(sentContent, moodLogRepository, userRepository, tgUI)
                .remindUsers();
        assertThat(result.iterator().next().getMarkup().getKeyboard()
                .iterator().next().iterator().next().getText()).isEqualTo("Good");
    }

    @Test
    public void whenUserVotedTodayThenNoReminderSent() {
        UserFakeRepository userRepository = new UserFakeRepository();

        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setClientId(100L);
        fakeUser.setChatId(12345L);
        userRepository.addUser(fakeUser);

        var result = new ArrayList<Content>();

        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };

        var moodLogRepository = new MoodLogFakeRepository();

        var moodLog = new MoodLog();
        moodLog.setUser(fakeUser);
        var todayStart = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        moodLog.setCreatedAt(todayStart + 1000);
        moodLogRepository.save(moodLog);

        var moodRepository = new MoodFakeRepository();
        var tgUI = new TgUI(moodRepository);

        new ReminderService(sentContent, moodLogRepository, userRepository, tgUI)
                .remindUsers();

        assertThat(result).isEmpty();
    }

    @Test
    public void whenNoUsersThenNoRemindersSent() {
        UserFakeRepository userRepository = new UserFakeRepository();

        MoodLogFakeRepository moodLogRepository = new MoodLogFakeRepository();

        var result = new ArrayList<Content>();

        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };

        MoodFakeRepository moodRepository = new MoodFakeRepository();
        TgUI tgUI = new TgUI(moodRepository);

        new ReminderService(sentContent, moodLogRepository, userRepository, tgUI)
                .remindUsers();

        assertThat(result).isEmpty();
    }

    @Test
    public void whenRemindingUsersThenContentHasCorrectTextAndButtons() {
        UserFakeRepository userRepository = new UserFakeRepository();

        User user = new User();
        user.setId(1L);
        user.setClientId(100L);
        user.setChatId(12345L);
        userRepository.addUser(user);

        MoodLogFakeRepository moodLogRepository = new MoodLogFakeRepository();

        MoodFakeRepository moodRepository = new MoodFakeRepository();
        moodRepository.save(new Mood("Good", true));
        moodRepository.save(new Mood("Bad", false));

        var result = new ArrayList<Content>();

        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };


        TgUI tgUI = new TgUI(moodRepository);
        ReminderService reminderService = new ReminderService(sentContent, moodLogRepository, userRepository, tgUI);

        reminderService.remindUsers();
        assertThat(result).isNotEmpty();

        Content content = result.get(0);

        String expectedText = "Как настроение?";
        assertThat(content.getText()).isEqualTo(expectedText);

        assertThat(content.getMarkup()).isNotNull();
        assertThat(content.getMarkup().getKeyboard()).isNotEmpty();
    }
}