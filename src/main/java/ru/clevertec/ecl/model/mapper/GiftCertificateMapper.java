package ru.clevertec.ecl.model.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.model.dto.GiftCertificateDto;
import ru.clevertec.ecl.model.entity.GiftCertificate;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificate dtoToEntity(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto entityToDto(GiftCertificate giftCertificate);
}
