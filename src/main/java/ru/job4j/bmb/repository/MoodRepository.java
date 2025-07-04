package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Mood;

import java.util.List;

@Repository
public interface MoodRepository extends CrudRepository<Mood, Long> {

    List<Mood> findAll();

    List<Mood> findByText(String text);

    List<Mood> findByGoodTrue();

    List<Mood> findByGoodFalse();

    List<Mood> findByTextContainingIgnoreCase(String substring);

    void deleteByText(String text);
}
