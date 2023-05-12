package com.example.springjdbcsandbox.controller;

import com.example.springjdbcsandbox.dto.FileMetadataDto;
import com.example.springjdbcsandbox.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/metadata/{metadataId}")
    public FileMetadataDto getMetadata(@PathVariable Long metadataId) {
        return metadataService.getMetadata(metadataId);
    }

    @PutMapping(value = "/metadata")
    public FileMetadataDto createMetadata(@RequestBody FileMetadataDto metadata) {
        log.debug("Creating metadata: {}", metadata);
        return metadataService.createMetadata(metadata);
    }

    @PutMapping(value = "/metadata/batch")
    public int[] batchCreateMetadata(@Valid @RequestBody List<FileMetadataDto> metadataList) {
        log.debug("Creating batch metadata: {}", metadataList);
        return metadataService.batchCreateMetadata(metadataList);
    }
}
