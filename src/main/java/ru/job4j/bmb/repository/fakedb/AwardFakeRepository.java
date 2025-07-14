package ru.job4j.bmb.repository.fakedb;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.repository.AwardRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AwardFakeRepository extends CrudRepositoryFake<Award, Long> implements AwardRepository {
    public final List<Award> storage = new ArrayList<>();

    @Override
    public List<Award> findAll() {
        return new ArrayList<>(memory.values());
    }
}
