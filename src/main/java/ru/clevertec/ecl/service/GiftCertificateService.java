package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.GiftCertificateDto;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    Long saveGiftCertificate(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> findAllGiftCertificates(Integer page, Integer pageSize);

    GiftCertificateDto findGiftCertificateById(long id);

    List<GiftCertificateDto> findByParameters(String tagName, String substring, String dateSortType, String nameSortType);

    GiftCertificateDto updateGiftCertificate(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto updateGiftCertificatePartially(GiftCertificateDto giftCertificateDto);

    void removeGiftCertificate(GiftCertificateDto giftCertificateDto);
}
