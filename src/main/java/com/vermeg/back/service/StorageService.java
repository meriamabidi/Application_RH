package com.vermeg.back.service;


import com.vermeg.back.entity.Conge;
import com.vermeg.back.entity.FileData;
import com.vermeg.back.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {


    @Autowired
    private FileDataRepository fileDataRepository;

    private final String STATIC_FOLDER_PATH = "C:/Users/HP/Downloads/back/src/main/resources/static/MyFiles/";

    public String uploadImageToFileSystem(MultipartFile file , FileData fileD) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String filePath = STATIC_FOLDER_PATH + File.separator + fileName;

        File destinationFile = new File(filePath);

        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        file.transferTo(destinationFile);

        FileData fileData = fileDataRepository.save(FileData.builder()
                .id(fileD.getId())
                .name(fileName)
                .type(fileD.getType())
                .employeeId(fileD.getId_emp())
                .filePath(filePath)
                .build());

        if (fileData != null) {
            return "File uploaded successfully: " + fileName;
        }

        return "File upload failed.";
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public List<FileData> getAllFileData() {
        return fileDataRepository.findAll();
    }

    public Optional<FileData> findById(Long id)
    {
        return fileDataRepository.findById(id);
    }

    public void save(FileData fileData) {
        fileDataRepository.save(fileData);
    }

    public List<FileData> getAllDocByEmpId(Long empId) {
        return fileDataRepository.findByEmployeeId(empId);
    }


}