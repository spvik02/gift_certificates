package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    private TagService tagService;
    @Mock
    private TagRepository tagRepository;

    private TagDto tagExpected = new TagDto(1L, "Test");

    @BeforeEach
    void setUp() {
        this.tagService = new TagServiceImpl(tagRepository);
    }

    @Nested
    class FindAllTags {

        @Test
        void checkFindAllTagsShouldReturnListTagDto() {
            List<Tag> tags = new ArrayList<>();
            tags.add(new Tag(1L, "Test"));
            doReturn(tags).when(tagRepository).findAll(1, 20);

            List<TagDto> actualTags = tagService.findAllTags(1, 20);

            assertThat(actualTags.stream()
                    .anyMatch(actualTagDto -> actualTagDto.equals(tagExpected))
            ).isTrue();
        }
    }

    @Nested
    class FindTagById {
        @Test
        void checkFindTagByIdShouldReturnTagDto() {
            doReturn(Optional.of(new Tag(1L, "Test"))).when(tagRepository).findById(1L);

            TagDto actualTag = tagService.findTagById(1L);

            assertThat(actualTag).isEqualTo(tagExpected);
        }

        @Test
        void checkFindByIdShouldThrowNoSuchElementException() {
            doReturn(Optional.empty()).when(tagRepository).findById(1L);

            assertThatThrownBy(() -> {
                tagService.findTagById(1L);
            }).isInstanceOf(NoSuchElementException.class).hasMessageContaining("tag with id 1 wasn't found");
        }
    }


    @Test
    void updateTag() {
        doReturn(new Tag(1L, "Test")).when(tagRepository).update(any(Tag.class));

        tagService.updateTag(new TagDto());

        verify(tagRepository, times(1)).update(any(Tag.class));
    }

    @Nested
    class UpdateTagPartially {
        @Test
        void updateTagPartially() {
            doReturn(Optional.of(new Tag())).when(tagRepository).findById(1L);
            doReturn(new Tag(1L, "Test")).when(tagRepository).update(any(Tag.class));

            tagService.updateTagPartially(new TagDto(1L, "TestOld"));

            verify(tagRepository, times(1)).update(any(Tag.class));
        }

        @Test
        void checkFindByIdShouldThrowNoSuchElementException() {
            doReturn(Optional.empty()).when(tagRepository).findById(any(Long.class));

            assertThatThrownBy(() -> {
                tagService.updateTagPartially(new TagDto(1L, "TestOld"));
            }).isInstanceOf(NoSuchElementException.class).hasMessageContaining("tag with id 1 wasn't found");
        }
    }

    @Test
    void saveTag() {
        doReturn(1L).when(tagRepository).save(any(Tag.class));

        tagService.saveTag(new TagDto(1L, "TestOld"));

        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void removeTag() {
        doNothing().when(tagRepository).remove(any(Tag.class));

        tagService.removeTag(new TagDto(1L, "TestOld"));

        verify(tagRepository, times(1)).remove(any(Tag.class));
    }
}