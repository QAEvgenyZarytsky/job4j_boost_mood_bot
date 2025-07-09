package ru.job4j.bmb.repository.fakedb;

import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.*;

public class UserFakeRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void addAllUsers(List<User> users) {
        users.forEach(this::addUser);
    }

    @Override
    public User findByClientId(Long clientId) {
        return users.values().stream()
                .filter(user -> user.getClientId() == clientId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public <S extends User> S save(S entity) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }
}

