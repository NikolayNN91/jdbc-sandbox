package com.example.springjdbcsandbox.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
public class FileMetadata implements Serializable {

    @Id
    private Long id;
    private String name;
    private Long size;
    private String md5Hash;
    private String contentType;

}
