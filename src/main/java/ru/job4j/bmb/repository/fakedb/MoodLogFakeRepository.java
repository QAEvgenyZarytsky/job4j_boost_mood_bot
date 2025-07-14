package ru.job4j.bmb.repository.fakedb;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("moodFakeRepository")
public class MoodLogFakeRepository
        extends CrudRepositoryFake<MoodLog, Long>
        implements MoodLogRepository {

    private long count = 0;

    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public <S extends MoodLog> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++count);
        }
        return super.save(entity);
    }

    @Override
    public List<MoodLog> findByUserId(Long userId) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser() != null && userId.equals(moodLog.getUser().getId()))
                .collect(Collectors.toList());
    }

    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay, List<User> allUsers) {
        var usersVotedToday = memory.values().stream()
                .filter(moodLog -> moodLog.getCreatedAt() >= startOfDay && moodLog.getCreatedAt() <= endOfDay)
                .map(MoodLog::getUser)
                .collect(Collectors.toSet());

        return allUsers.stream()
                .filter(user -> !usersVotedToday.contains(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoodLog> findByUserAndCreatedAtAfter(User user, long oneWeekAgo) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().equals(user))
                .filter(moodLog -> moodLog.getCreatedAt() >= oneWeekAgo)
                .collect(Collectors.toList());
    }
}