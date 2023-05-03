package ru.clevertec.ecl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@RestController
@Component
@RequestMapping(value = "/api/tags")
public class TagController {
    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    /**
     * Returns list of Tag within the specified range
     *
     * @param page     value in range [1..n]
     * @param pageSize the number of certificates per page
     * @return ResponseEntity with list of tags
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TagDto>> findAllTags(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<TagDto> tags = tagService.findAllTags(page, pageSize);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * Finds tag with provided id
     *
     * @param id tag id
     * @return ResponseEntity with tag
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findTagById(@PathVariable Long id) {
        TagDto tag = tagService.findTagById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Creates new Tag with provided data and returns generated id
     *
     * @param tagDto Tag data
     * @return ResponseEntity with generated id
     */
    @PostMapping
    public ResponseEntity<Long> createTag(@RequestBody TagDto tagDto) {
        long id = tagService.saveTag(tagDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Updates Tag with data passed in request body. Updates all fields with new data
     *
     * @param tagDto request body containing Tag data for update
     * @return ResponseEntity with updated tag
     */
    @PutMapping
    public ResponseEntity<TagDto> updateTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.updateTag(tagDto);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Updates Tag with data passed in request body. If field is not specified in request body the data is not updated
     *
     * @param tagDto request body containing Tag data for update
     * @return ResponseEntity with updated tag
     */
    @PatchMapping
    public ResponseEntity<TagDto> updateTagPartially(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.updateTagPartially(tagDto);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Deletes passed Tag. Body can contain only id
     *
     * @param tagDto request body containing GiftCertificate data
     * @return empty ResponseEntity
     */
    @DeleteMapping
    public ResponseEntity<Void> removeTag(@RequestBody TagDto tagDto) {
        tagService.removeTag(tagDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
