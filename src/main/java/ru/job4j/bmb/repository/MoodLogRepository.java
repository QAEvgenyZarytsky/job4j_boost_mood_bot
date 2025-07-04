package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {

    List<MoodLog> findAll();

    List<MoodLog> findByUser(User user);

    List<MoodLog> findByMood(Mood mood);

    long countByUser(User user);

    void deleteByUser(User user);

    void deleteByMood(Mood mood);
}
