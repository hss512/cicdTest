package com.example.easyplan.service;

import com.example.easyplan.exception.FileStorageException;
import com.example.easyplan.exception.MyFileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileStorageServiceImpl implements FileStorageService{
    private final Path fileStorageLocation;

    public FileStorageServiceImpl(){
        this.fileStorageLocation = Paths.get("C:\\boot\\easy-plan\\frontend\\public")
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("can not create file store path", ex);
        }
    }

    @Override
    public String[] storeFile(MultipartFile file) {
        String[] result = new String[2];
        // 파일명 정규화
        result[0] = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("fileName: " + result[0]);
        try {
            // 파일 저장 경로 결정 및 파일 저장
            Path targetLocation = this.fileStorageLocation.resolve(result[0]);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            result[1] = targetLocation.toString().replace("\\", "/");
            log.info("path: " + result[1]);
            return result;
        } catch (IOException ex) {
            throw new FileStorageException("file " + result[0] + "can not store. plz try again", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            // 파일의 저장 경로를 Path 객체로 생성
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            // 파일의 Path로 Resource 객체를 생성
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
