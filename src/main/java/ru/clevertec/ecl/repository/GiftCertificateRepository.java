package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    long save(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll(int page, int pageSize);

    List<GiftCertificate> findByParameters(String tagName, String substring, String dateSortType, String nameSortType);

    Optional<GiftCertificate> findById(long id);

    GiftCertificate update(GiftCertificate giftCertificate);

    void remove(GiftCertificate giftCertificate);
}
