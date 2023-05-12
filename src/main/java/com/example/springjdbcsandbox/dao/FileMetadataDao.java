package com.example.springjdbcsandbox.dao;

import com.example.springjdbcsandbox.entity.FileMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FileMetadataDao {

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String SIZE_COLUMN = "size";
    private static final String MD5_HASH_COLUMN = "md5_hash";
    private static final String CONTENT_TYPE_COLUMN = "content_type";

    private final JdbcTemplate jdbcTemplate;

    public FileMetadataDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FileMetadata save(FileMetadata entity) {
        String query = "insert into file_metadata (`name`, `size`, md5_hash, content_type) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setLong(2, entity.getSize());
                ps.setString(3, entity.getMd5Hash());
                ps.setString(4, entity.getContentType());
                return ps;
            }
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return createNewFrom(keyHolder.getKey().longValue(), entity);
    }

    public int[] saveAll(List<FileMetadata> entities) {
        String query = "insert into file_metadata (`name`, `size`, md5_hash, content_type) values (?, ?, ?, ?)";
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, entities.get(i).getName());
                ps.setLong(2, entities.get(i).getSize());
                ps.setString(3, entities.get(i).getMd5Hash());
                ps.setString(4, entities.get(i).getContentType());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        };
        return jdbcTemplate.batchUpdate(query, batchPreparedStatementSetter);
    }

    // with NamedParameterJdbcTemplate
//    public List<FileMetadata> saveAllUsingUtils(List<FileMetadata> entities) {
//        String query = "insert into file_metadata (`name`, `size`, md5_hash, content_type) values (?, ?, ?, ?)";
//        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(entities.toArray());
//        return jdbcTemplate.batchUpdate(query, batch);
//    }

    public Optional<FileMetadata> findById(Long metadataId) {
        String query = "select * from file_metadata where id = ?";
        RowMapper<FileMetadata> rowMapper = (rs, rowNum) -> {
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setId(rs.getLong(ID_COLUMN));
            fileMetadata.setName(rs.getString(NAME_COLUMN));
            fileMetadata.setSize(rs.getLong(SIZE_COLUMN));
            fileMetadata.setMd5Hash(rs.getString(MD5_HASH_COLUMN));
            fileMetadata.setContentType(rs.getString(CONTENT_TYPE_COLUMN));
            return fileMetadata;
        };
        FileMetadata metadata = null;
        List<FileMetadata> metadatas = null;
        try {
            metadatas = jdbcTemplate.query(query, rowMapper, metadataId);
            metadata = jdbcTemplate.queryForObject(query, rowMapper, metadataId);
        } catch (EmptyResultDataAccessException e) {
            // do nothing
        }
        log.info("Find by id result: {}", metadata);
        return Optional.ofNullable(metadata);
    }

    private FileMetadata createNewFrom(Long id, FileMetadata fileMetadata) {
        FileMetadata newFileMetadata = new FileMetadata();
        newFileMetadata.setId(id);
        newFileMetadata.setName(fileMetadata.getName());
        newFileMetadata.setSize(fileMetadata.getSize());
        newFileMetadata.setMd5Hash(fileMetadata.getMd5Hash());
        newFileMetadata.setContentType(fileMetadata.getContentType());
        return newFileMetadata;
    }
}
