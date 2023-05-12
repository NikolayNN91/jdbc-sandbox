package com.example.springjdbcsandbox.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
@Setter
@Getter
public class FileMetadataDto {

    private final static ObjectMapper mapper = new ObjectMapper();

    private Long id;
    @NonNull
    private String name;
    @NonNull
    private long size;
    @NonNull
    private String md5Hash;
    @NonNull
    private String contentType;

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Error on toString: FileMetadataDto");
            return "ERROR";
        }
    }
}
