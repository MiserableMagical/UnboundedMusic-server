package com.server.service;

import com.server.entity.FileRecord;
import com.server.repository.FileRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    private final FileRecordRepository repository;

    public FileService(FileRecordRepository repository) {
        this.repository = repository;
    }

    public void saveRecord(FileRecord record) {
        repository.save(record);
    }

    public Optional<FileRecord> getRecord(String id) {
        return repository.findById(id);
    }
}