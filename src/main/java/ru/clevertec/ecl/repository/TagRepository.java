package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Long save(Tag tag);

    List<Tag> findAll(int page, int pageSize);

    Optional<Tag> findById(long id);

    Tag update(Tag tag);

    void remove(Tag tag);
}
