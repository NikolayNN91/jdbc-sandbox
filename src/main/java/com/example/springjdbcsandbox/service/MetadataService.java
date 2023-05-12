package com.example.springjdbcsandbox.service;

import com.example.springjdbcsandbox.dao.FileMetadataDao;
import com.example.springjdbcsandbox.dto.FileMetadataDto;
import com.example.springjdbcsandbox.entity.FileMetadata;
import com.example.springjdbcsandbox.exception.BadRequestException;
import com.example.springjdbcsandbox.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetadataService {

    private static final ModelMapper modelMapper = new ModelMapper();

    private final FileMetadataDao fileMetadataDao;

    public MetadataService(FileMetadataDao fileMetadataDao) {
        this.fileMetadataDao = fileMetadataDao;
    }

    public FileMetadataDto createMetadata(FileMetadataDto metadata) {
        FileMetadata entity = convertToEntity(metadata);
        validate(entity);
        FileMetadata saved = fileMetadataDao.save(entity);
        return convertToDto(saved);
    }

    public int[] batchCreateMetadata(List<FileMetadataDto> metadataList) {
        List<FileMetadata> entities = convertToEntityList(metadataList);
        entities.forEach(this::validate);
        return fileMetadataDao.saveAll(entities);
    }

    public FileMetadataDto getMetadata(Long metadataId) {
        return fileMetadataDao.findById(metadataId)
                .map(this::convertToDto)
                .orElseThrow(NotFoundException::new);
    }

    private FileMetadataDto convertToDto(FileMetadata metadata) {
        return modelMapper.map(metadata, FileMetadataDto.class);
    }

    private List<FileMetadataDto> convertToDto(List<FileMetadata> metadataList) {
        return metadataList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private FileMetadata convertToEntity(FileMetadataDto metadata) {
        return modelMapper.map(metadata, FileMetadata.class);
    }

    private List<FileMetadata> convertToEntityList(List<FileMetadataDto> metadataList) {
        return metadataList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    private void validate(FileMetadata metadata) {
        if (metadata.getId() != null) {
            throw new BadRequestException();
        }
    }

}
