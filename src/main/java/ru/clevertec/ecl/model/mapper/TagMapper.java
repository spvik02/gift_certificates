package ru.clevertec.ecl.model.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.Tag;

@Mapper
public interface TagMapper {

    Tag dtoToEntity(TagDto tagDto);

    TagDto entityToDto(Tag tag);
}
