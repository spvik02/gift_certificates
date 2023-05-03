package ru.clevertec.ecl.service.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.model.dto.GiftCertificateDto;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Long saveGiftCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.dtoToEntity(giftCertificateDto);
        return giftCertificateRepository.save(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> findAllGiftCertificates(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(page, pageSize);
        return giftCertificates.stream()
                .map(giftCertificate -> giftCertificateMapper.entityToDto(giftCertificate))
                .toList();
    }

    @Override
    public GiftCertificateDto findGiftCertificateById(long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateRepository
                .findById(id)
                .map(giftCertificate -> giftCertificateMapper.entityToDto(giftCertificate))
                .orElseThrow(() -> new NoSuchElementException("gift certificate with id " + id + " wasn't found"));
        return giftCertificateDto;
    }

    @Override
    public List<GiftCertificateDto> findByParameters(String tagName, String substring,
                                                     String dateSortType, String nameSortType) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository
                .findByParameters(tagName, substring, dateSortType, nameSortType);
        return giftCertificates.stream()
                .map(giftCertificate -> giftCertificateMapper.entityToDto(giftCertificate))
                .toList();
    }

    @Override
    public GiftCertificateDto updateGiftCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateRepository
                .update(giftCertificateMapper.dtoToEntity(giftCertificateDto));
        return giftCertificateMapper.entityToDto(giftCertificate);
    }

    @Override
    public GiftCertificateDto updateGiftCertificatePartially(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificateForUpdate = giftCertificateRepository.findById(giftCertificateDto.getId())
                .map(giftCertificate -> {
                    if (giftCertificateDto.getName() != null)
                        giftCertificate.setName(giftCertificateDto.getName());
                    if (giftCertificateDto.getDescription() != null)
                        giftCertificate.setDescription(giftCertificateDto.getDescription());
                    if (giftCertificateDto.getDuration() != 0)
                        giftCertificate.setDuration(giftCertificateDto.getDuration());
                    if (giftCertificateDto.getPrice() != null)
                        giftCertificate.setPrice(giftCertificateDto.getPrice());
                    return giftCertificate;
                })
                .orElseThrow(() -> new NoSuchElementException(
                        "gift certificate with id " + giftCertificateDto.getId() + " wasn't found"));

        GiftCertificate updatedGiftCertificate = giftCertificateRepository.update(giftCertificateForUpdate);
        return giftCertificateMapper.entityToDto(updatedGiftCertificate);
    }

    @Override
    public void removeGiftCertificate(GiftCertificateDto giftCertificateDto) {
        try {
            giftCertificateRepository.remove(giftCertificateMapper.dtoToEntity(giftCertificateDto));
        } catch (Exception e) {
            throw new NoSuchElementException(
                    "gift certificate with id " + giftCertificateDto.getId() + " wasn't found");
        }
    }
}
