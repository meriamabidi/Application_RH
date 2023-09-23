package com.vermeg.back.repository;

import com.vermeg.back.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Long> {
    Optional<FileData> findByName(String fileName);
    Optional<FileData> findById(Long id);

    List<FileData> findByEmployeeId(Long empId);





}