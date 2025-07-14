package ru.job4j.bmb.repository.fakedb;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AchievementFakeRepository extends CrudRepositoryFake<Achievement, Integer> implements AchievementRepository {
    public final List<Achievement> storage = new ArrayList<>();

    @Override
    public List<Achievement> findAll() {
        return new ArrayList<>(memory.values());
    }


    @Override
    public List<Achievement> findByUser(User user) {
        return memory.values().stream()
                .filter(achievement -> achievement.getUser() != null && achievement.getUser().equals(user))
                .collect(Collectors.toList());
    }
}
