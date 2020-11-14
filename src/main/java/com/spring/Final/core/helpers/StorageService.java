package com.spring.Final.core.helpers;

import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@Service
public class StorageService {

    @Value("${upload.file.directory}")
    private String uploadDir;

    @Value("${upload.static-directory}")
    private String staticDirectory;

    @Value("${server.domain}")
    private String serverDomain;

    private Path storageLocation;

    public HashMap<String, String> store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("EMPTY_FILE", "File is empty");
            }
            // create directory if not exists
            File directory = new File(uploadDir);
            if (! directory.exists()){
                directory.mkdir();
            }

            String[] segmentParts = file.getOriginalFilename().split("\\.");
            String extension = segmentParts[segmentParts.length - 1];
            String fileName = CommonHelper.getAlphaNumericString(20) + "." + extension;

            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            String storageUrl = serverDomain + "/" + staticDirectory + "/" + fileName;

            HashMap<String, String> data = new HashMap<>();
            data.put("name", file.getOriginalFilename());
            data.put("path", storageUrl);

            return data;
        } catch (IOException e) {
            throw new StorageException("STORE_FILE_FAILED", e.getMessage());
        }
    }

    /**
     * @Document: https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
     */
    public Resource loadFileAsResource(String fileName) {
        if (this.storageLocation == null) {
            this.storageLocation = Paths.get(this.uploadDir).toAbsolutePath().normalize();
        }
        try {
            Path filePath = this.storageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException();
        }
    }
}
