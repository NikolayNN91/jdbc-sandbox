package com.example.springjdbcsandbox.repository;

import com.example.springjdbcsandbox.entity.FileMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends CrudRepository<FileMetadata, Long> {

}
