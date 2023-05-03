package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.model.dto.GiftCertificateDto;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    private GiftCertificateDto testCertificate;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository);
        testCertificate = new GiftCertificateDto(
                1L, "Test", "test descr", BigDecimal.valueOf(1.99), 7, new ArrayList<>());
    }

    @Test
    void SaveGiftCertificate() {
        doReturn(1L).when(giftCertificateRepository).save(any(GiftCertificate.class));

        giftCertificateService.saveGiftCertificate(testCertificate);

        verify(giftCertificateRepository, times(1)).save(any(GiftCertificate.class));
    }

    @Test
    void findAllGiftCertificates() {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(
                new GiftCertificate("Test", "test descr", BigDecimal.valueOf(1.99), 7));
        doReturn(giftCertificates).when(giftCertificateRepository).findAll(1, 20);

        List<GiftCertificateDto> actualTags = giftCertificateService.findAllGiftCertificates(1, 20);

        verify(giftCertificateRepository, times(1)).findAll(any(Integer.class), any(Integer.class));
    }

    @Nested
    class FindGiftCertificateById {
        @Test
        void checkFindByIdShouldThrowNoSuchElementException() {
            doReturn(Optional.empty()).when(giftCertificateRepository).findById(1L);

            assertThatThrownBy(() -> {
                giftCertificateService.findGiftCertificateById(1L);
            }).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    void removeGiftCertificate() {
        doNothing().when(giftCertificateRepository).remove(any(GiftCertificate.class));

        giftCertificateService.removeGiftCertificate(testCertificate);

        verify(giftCertificateRepository, times(1)).remove(any(GiftCertificate.class));
    }
}
