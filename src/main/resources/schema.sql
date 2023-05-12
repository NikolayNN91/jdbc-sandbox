CREATE TABLE file_metadata (
                         id INTEGER NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(128) NOT NULL,
                         `size` INTEGER NOT NULL,
                         md5_hash VARCHAR(256) NOT NULL,
                         content_type VARCHAR(32) NOT NULL,
                         PRIMARY KEY (id)
);