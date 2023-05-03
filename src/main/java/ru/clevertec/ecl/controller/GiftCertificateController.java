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
import ru.clevertec.ecl.model.dto.GiftCertificateDto;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

@RestController
@Component
@RequestMapping(value = "/api/certificates")
public class GiftCertificateController {

    private GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Creates new GiftCertificate with provided data and returns generated id
     *
     * @param giftCertificateDto GiftCertificate data
     * @return genereted id
     */
    @PostMapping
    public ResponseEntity<Long> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        long id = giftCertificateService.saveGiftCertificate(giftCertificateDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Returns list of GiftCertificate within the specified range
     *
     * @param page     value in range [1..n]
     * @param pageSize the number of certificates per page
     * @return list of GiftCertificate
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<GiftCertificateDto>> findAllGiftCertificates(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findAllGiftCertificates(page, pageSize);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Return gift certificate with provided id
     *
     * @param id gift certificate id
     * @return gift certificate with provided id
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findGiftCertificateById(@PathVariable Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findGiftCertificateById(id);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    /**
     * Returns gift certificates filtered and sorted by passed parameters
     *
     * @param tagName      full tag name
     * @param substring    part of a string to search by name/description
     * @param dateSortType ordering type for date (asc/desc)
     * @param nameSortType ordering type for name (asc/desc)
     * @return list of certificates matching the passed parameters
     */
    @GetMapping("/filter")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByParameters(
            @RequestParam(value = "tagName", required = false) String tagName,
            @RequestParam(value = "substring", required = false) String substring,
            @RequestParam(value = "dateSortType", required = false) String dateSortType,
            @RequestParam(value = "nameSortType", required = false) String nameSortType) {
        List<GiftCertificateDto> giftCertificates = giftCertificateService
                .findByParameters(tagName, substring, dateSortType, nameSortType);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Updates GiftCertificate with data passed in request body. Updates all fields with new data
     *
     * @param giftCertificateDto request body containing GiftCertificate data
     * @return updated GiftCertificateDto
     */
    @PutMapping
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto giftCertificateUpdated = giftCertificateService.updateGiftCertificate(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateUpdated, HttpStatus.OK);
    }

    /**
     * Updates GiftCertificate with data passed in request body. If field is not specified in request body the data is not updated
     *
     * @param giftCertificateDto request body containing GiftCertificate data
     * @return updated GiftCertificateDto
     */
    @PatchMapping
    public ResponseEntity<GiftCertificateDto> updateGiftCertificatePartially(
            @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto giftCertificateUpdated = giftCertificateService
                .updateGiftCertificatePartially(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateUpdated, HttpStatus.OK);
    }

    /**
     * Deletes passed GiftCertificate. Body can contain only id
     *
     * @param giftCertificateDto request body containing GiftCertificate data
     * @return empry ResponseEntity
     */
    @DeleteMapping
    public ResponseEntity<Void> removeGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        giftCertificateService.removeGiftCertificate(giftCertificateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
