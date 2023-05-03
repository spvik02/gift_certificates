package ru.clevertec.ecl.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepositoryImpl;
import ru.clevertec.ecl.repository.impl.TagRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class InMemoryDBTest {

    private GiftCertificateRepository giftCertificateRepository;
    private final List<GiftCertificate> certificates = List.of(
            new GiftCertificate("spring", "certificate descr", BigDecimal.valueOf(165.05), 7),
            new GiftCertificate("spring2", "certificate descr2", BigDecimal.valueOf(165.05), 7),
            new GiftCertificate(null, "spring3", "certificate descr3", BigDecimal.valueOf(165.05),
                    7, null, null, List.of(new Tag("flowers")))
    );

    @BeforeEach
    void setUp() {
        giftCertificateRepository = new GiftCertificateRepositoryImpl(new TagRepositoryImpl());
    }

    @Test
    public void checkSaveShouldSaveGiftCertificate() {
        long id = giftCertificateRepository.save(certificates.get(0));

        Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id);

        assertThat(giftCertificateActual.get()).isNotNull();
    }

    @Nested
    class FindById {
        @Test
        public void checkFindByIdShouldReturnOptionalOfGiftCertificate() {
            GiftCertificate expectedGiftCertificate = new GiftCertificate(
                    "spring", "certificate descr", BigDecimal.valueOf(165.05), 7);
            long id = giftCertificateRepository.save(certificates.get(0));

            Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id);

            expectedGiftCertificate.setId(id);
            assertThat(giftCertificateActual.get().getId()).isEqualTo(expectedGiftCertificate.getId());
            assertThat(giftCertificateActual.get().getName()).isEqualTo(expectedGiftCertificate.getName());
            assertThat(giftCertificateActual.get().getDescription()).isEqualTo(expectedGiftCertificate.getDescription());
            assertThat(giftCertificateActual.get().getDuration()).isEqualTo(expectedGiftCertificate.getDuration());
            assertThat(giftCertificateActual.get().getPrice()).isEqualTo(expectedGiftCertificate.getPrice());
            assertThat(giftCertificateActual.get().getTags()).hasSize(expectedGiftCertificate.getTags().size());
            assertThat(giftCertificateActual.get().getTags().containsAll(expectedGiftCertificate.getTags())).isTrue();
        }

        @Test
        public void checkFindByIdShouldReturnEmptyOptional() {
            long id = giftCertificateRepository.save(certificates.get(0));

            Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id + 1);

            assertThat(giftCertificateActual).isEqualTo(Optional.empty());
        }
    }

    @Nested
    class FindAll {
        @Test
        public void checkFindAllShouldReturnListOfTwo() {
            giftCertificateRepository.save(certificates.get(0));
            giftCertificateRepository.save(certificates.get(1));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findAll(1, 20);

            assertThat(giftCertificateListActual).hasSize(2);
        }

        @Test
        public void checkFindAllShouldReturnListOfOne() {
            giftCertificateRepository.save(certificates.get(0));
            giftCertificateRepository.save(certificates.get(1));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findAll(1, 1);

            assertThat(giftCertificateListActual).hasSize(1);
        }
    }

    @Nested
    class Update {
        @Test
        public void checkUpdateShouldReturnUpdatedGiftCertificate() {
            long id = giftCertificateRepository.save(certificates.get(0));
            Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
            giftCertificateOptional.get().setName("springUpdated");
            giftCertificateRepository.update(giftCertificateOptional.get());
            Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id);

            assertThat(giftCertificateActual.get().getName()).isEqualTo("springUpdated");
        }

        @Test
        public void checkUpdateShouldThrowExceptionBecauseRowCount() {
            GiftCertificate giftCertificateTest = new GiftCertificate(
                    "springUpdated", "certificate descr", BigDecimal.valueOf(165.05), 7);
            giftCertificateTest.setId(1L);

            assertThatThrownBy(() -> giftCertificateRepository.update(giftCertificateTest))
                    .hasMessageContaining("actual row count: 0");
        }

        @Test
        public void checkUpdateShouldUpdateOtherElementsToNull() {
            GiftCertificate giftCertificateTest = new GiftCertificate();
            giftCertificateTest.setName("springUpdated");
            long id = giftCertificateRepository.save(certificates.get(0));
            giftCertificateTest.setId(id);

            giftCertificateRepository.update(giftCertificateTest);
            Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id);

            assertThat(giftCertificateActual.get().getDescription()).isEqualTo(null);
        }
    }

    @Test
    public void checkRemoveShouldRemove() {
        GiftCertificate giftCertificateTest = new GiftCertificate();
        long id = giftCertificateRepository.save(certificates.get(0));
        giftCertificateTest.setId(id);
        giftCertificateRepository.remove(giftCertificateTest);

        Optional<GiftCertificate> giftCertificateActual = giftCertificateRepository.findById(id);

        assertThat(giftCertificateActual).isEqualTo(Optional.empty());
    }

    @Nested
    class FindByParameters {
        @Test
        public void checkFindByParametersWithSubstringShouldReturnListOfOne() {
            giftCertificateRepository.save(certificates.get(0));
            giftCertificateRepository.save(certificates.get(1));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findByParameters(
                    null, "2", null, null);

            assertThat(giftCertificateListActual).hasSize(1);
        }

        @Test
        public void checkFindByParametersWithTagNameShouldReturnListOfOne() {
            giftCertificateRepository.save(certificates.get(0));
            giftCertificateRepository.save(certificates.get(2));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findByParameters(
                    "flowers", null, null, null);

            assertThat(giftCertificateListActual).hasSize(1);
        }

        @Test
        public void checkFindByParametersWithDateSortTypeShouldReturnRightOrder() {
            giftCertificateRepository.save(certificates.get(0));
            long idOfLastCreated = giftCertificateRepository.save(certificates.get(1));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findByParameters(
                    null, null, "desc", null);

            assertThat(giftCertificateListActual.get(0).getId()).isEqualTo(idOfLastCreated);
        }

        @Test
        public void checkFindByParametersWithNameSortTypeShouldReturnRightOrder() {
            giftCertificateRepository.save(certificates.get(0));
            long idOfBiggerName = giftCertificateRepository.save(certificates.get(1));

            List<GiftCertificate> giftCertificateListActual = giftCertificateRepository.findByParameters(
                    null, null, null, "desc");

            assertThat(giftCertificateListActual.get(0).getId()).isEqualTo(idOfBiggerName);
        }
    }
}
