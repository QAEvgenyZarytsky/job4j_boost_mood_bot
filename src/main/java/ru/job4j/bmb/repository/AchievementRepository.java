package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.User;

import java.util.List;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    List<Achievement> findAll();

    List<Achievement> findByCreateAtGreaterThan(long timestamp);

    List<Achievement> findByUser(User user);

    List<Achievement> findByAward(Award award);

    long countByUser(User user);

    void deleteByAward(Award award);


}
