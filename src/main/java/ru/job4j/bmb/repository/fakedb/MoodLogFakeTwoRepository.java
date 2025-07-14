package ru.job4j.bmb.repository.fakedb;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MoodLogFakeTwoRepository extends CrudRepositoryFake<MoodLog, Long> implements MoodLogRepository {
    private long idSequence = 0;

    @Override
    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public <S extends MoodLog> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++idSequence);
        }
        return super.save(entity);
    }


    @Override
    public List<MoodLog> findByUserId(Long userId) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser() != null && userId.equals(moodLog.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoodLog> findByUserAndCreatedAtAfter(User user, long oneWeekAgo) {
        return List.of();
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay, List<User> allUsers) {
        return List.of();
    }
}
