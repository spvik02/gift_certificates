package ru.clevertec.ecl.service.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.model.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> findAllTags(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        List<Tag> tags = tagRepository.findAll(page, pageSize);
        return tags.stream()
                .map(tag -> tagMapper.entityToDto(tag))
                .toList();
    }

    @Override
    public TagDto findTagById(long id) {
        TagDto tagDto = tagRepository
                .findById(id)
                .map(tag -> tagMapper.entityToDto(tag))
                .orElseThrow(() -> new NoSuchElementException("tag with id " + id + " wasn't found"));
        return tagDto;
    }

    @Override
    public TagDto updateTag(TagDto tagDto) {
        Tag tag = tagRepository.update(tagMapper.dtoToEntity(tagDto));
        return tagMapper.entityToDto(tag);
    }

    @Override
    public TagDto updateTagPartially(TagDto tagDto) {
        Tag tagForUpdate = tagRepository.findById(tagDto.getId())
                .map(tag -> {
                    Optional.ofNullable(tagDto.getName()).ifPresent(tag::setName);
                    return tag;
                })
                .orElseThrow(() -> new NoSuchElementException("tag with id " + tagDto.getId() + " wasn't found"));

        Tag updatedTag = tagRepository.update(tagForUpdate);
        return tagMapper.entityToDto(updatedTag);
    }

    @Override
    public Long saveTag(TagDto tagDto) {
        return tagRepository.save(tagMapper.dtoToEntity(tagDto));
    }

    @Override
    public void removeTag(TagDto tagDto) {
        try {
            tagRepository.remove(tagMapper.dtoToEntity(tagDto));
        } catch (Exception e) {
            throw new NoSuchElementException("tag with id " + tagDto.getId() + " wasn't found");
        }
    }
}
