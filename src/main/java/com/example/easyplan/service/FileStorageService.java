package com.example.easyplan.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface FileStorageService {
    public String[] storeFile(MultipartFile file);
    public Resource loadFileAsResource(String fileName);

}