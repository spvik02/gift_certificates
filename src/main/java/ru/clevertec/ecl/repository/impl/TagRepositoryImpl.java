package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Component
public class TagRepositoryImpl implements TagRepository {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Override
    public List<Tag> findAll(int page, int pageSize) {
        List<Tag> selectTFromTagT;
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            selectTFromTagT = session
                    .createQuery("select t from Tag t", Tag.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(page * pageSize)
                    .list();

            session.getTransaction().commit();
        }
        return selectTFromTagT;
    }

    @Override
    public Optional<Tag> findById(long id) {
        Tag tag;
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            tag = session.get(Tag.class, id);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public Tag update(Tag tag) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(tag);
            session.getTransaction().commit();
        }
        return tag;
    }

    @Override
    public Long save(Tag tag) {
        Long resultId;
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            resultId = (Long) session.save(tag);
            session.getTransaction().commit();
        }
        return resultId;
    }

    public void remove(Tag tag) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete("tag", tag);
            session.getTransaction().commit();
        }

    }
}
