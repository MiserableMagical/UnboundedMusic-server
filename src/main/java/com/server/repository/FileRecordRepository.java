package com.server.repository;

import com.server.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository
        extends JpaRepository<FileRecord, String> {
}