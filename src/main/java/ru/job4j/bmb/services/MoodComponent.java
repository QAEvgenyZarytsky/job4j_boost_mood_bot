package ru.job4j.bmb.services;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MoodComponent {

    private final Map<String, String> moodList = Map.of(
            "lost_sock", "Носки — это коварные создания. Но не волнуйся, второй обязательно найдётся!",
            "cucumber", "Огурец тоже дело серьёзное! Главное, не мариноваться слишком долго.",
            "dance_ready", "Супер! Танцуй, как будто никто не смотрит. Или, наоборот, как будто все смотрят!",
            "need_coffee", "Кофе уже в пути! Осталось только подождать... И ещё немного подождать...",
            "sleepy", "Пора на боковую! Даже супергерои отдыхают, ты не исключение."
    );

    public Map<String, String> getMoodList() {
        return moodList;
    }

    public String getMoodText(String key) {
        return moodList.getOrDefault(key, "Неизвестное настроение");
    }
}