package ru.clevertec.ecl.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.HibernateUtil;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private TagRepository tagRepository;

    @Autowired
    public GiftCertificateRepositoryImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int pageSize) {
        List<GiftCertificate> certificates;
        try (Session session = sessionFactory.openSession()) {
            certificates = session
                    .createQuery(
                            "select c from GiftCertificate c left join fetch c.tags t",
                            GiftCertificate.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(page * pageSize)
                    .list();
            return certificates;
        }
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        GiftCertificate certificate;
        try (Session session = sessionFactory.openSession()) {
            certificate = session
                    .createQuery(
                            "select c from GiftCertificate c left join fetch c.tags t where c.id = :id",
                            GiftCertificate.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            certificate = null;
        }
        return Optional.ofNullable(certificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

    public long save(GiftCertificate giftCertificate) {
        long resultId;
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            for (Tag tag : giftCertificate.getTags()) {
                tag.getCertificates().add(giftCertificate);
                if (tag.getId() == null)
                    tagRepository.save(tag);
            }

            resultId = (long) session.save(giftCertificate);
            session.getTransaction().commit();
        }
        return resultId;
    }

    public void remove(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete("gift_certificate", giftCertificate);
            session.getTransaction().commit();
        }
    }

    public List<GiftCertificate> findByParameters(
            String tagName, String substring, String dateSortType, String nameSortType) {
        List<GiftCertificate> certificates;
        String queryString = prepareQuery(tagName, substring, dateSortType, nameSortType);

        try (Session session = sessionFactory.openSession()) {
            Query<GiftCertificate> query = session.createQuery(queryString, GiftCertificate.class);
            certificates = query.list();
            return certificates;
        } catch (NoResultException e) {
            return null;
        }
    }

    private String prepareQuery(String tagName, String substring, String dateSortType, String nameSortType) {
        StringBuilder queryStringBuilder = new StringBuilder();
        int filterNum = 0;
        int sortNum = 0;
        if (tagName != null) filterNum++;
        if (substring != null) filterNum++;
        if (dateSortType != null) sortNum++;
        if (nameSortType != null) sortNum++;

        queryStringBuilder.append("""
                select distinct c from GiftCertificate c
                left join fetch c.tags t
                """);

        if (filterNum > 0) queryStringBuilder.append(" where ");
        if (tagName != null) queryStringBuilder.append(" t.name like ")
                .append("'").append(tagName).append("'");
        if (filterNum > 1) queryStringBuilder.append(" and ");
        if (substring != null) queryStringBuilder.append(" c.name like ")
                .append("'%").append(substring).append("%'")
                .append(" or c.description like ")
                .append("'%").append(substring).append("%'");
        if (sortNum > 0) queryStringBuilder.append(" ORDER BY ");
        if (dateSortType != null) queryStringBuilder.append(" c.createDate ").append(dateSortType);
        if (sortNum > 1) queryStringBuilder.append(", ");
        if (nameSortType != null) queryStringBuilder.append(" c.name ").append(nameSortType);

        return queryStringBuilder.toString();
    }
}
