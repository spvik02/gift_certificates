package ru.clevertec.ecl.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = {"id", "name", "description", "price", "duration", "tags"})
public class GiftCertificateDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private List<TagDto> tags;
}
