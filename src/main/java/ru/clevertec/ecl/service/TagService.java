package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.TagDto;

import java.util.List;

public interface TagService {

    Long saveTag(TagDto tagDto);

    List<TagDto> findAllTags(Integer page, Integer pageSize);

    TagDto findTagById(long id);

    TagDto updateTag(TagDto tagDto);

    TagDto updateTagPartially(TagDto tagDto);

    void removeTag(TagDto tagDto);
}
