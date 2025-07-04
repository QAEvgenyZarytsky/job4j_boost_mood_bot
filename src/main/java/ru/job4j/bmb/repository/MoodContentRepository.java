package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;

import java.util.List;

@Repository
public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {

    List<MoodContent> findAll();

    List<MoodContent> findByMood(Mood mood);

    List<MoodContent> findByTextContainingIgnoreCase(String substring);

    List<MoodContent> findByMoodAndTextContainingIgnoreCase(Mood mood, String substring);

    long countByMood(Mood mood);

    void deleteByMood(Mood mood);
}
