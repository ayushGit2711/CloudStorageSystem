package com.ayushspring.cloudstoragesystem.service;

import com.ayushspring.cloudstoragesystem.model.FileEntity;
import com.ayushspring.cloudstoragesystem.model.User;
import com.ayushspring.cloudstoragesystem.repository.FileRepository;
import com.ayushspring.cloudstoragesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "C:/Users/agoyal/uploads/";

    // Handle file upload logic
    public ResponseEntity<String> uploadFile(MultipartFile file, User user) {
        try {
            long fileSize = file.getSize();
            long currentStorage = user.getCurrentStorage();

            // Check if storage limit is exceeded
            if (currentStorage + fileSize > user.getStorageLimit()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Storage limit exceeded.");
            }

            // Save the file to the file system
            String filePath = UPLOAD_DIR + file.getOriginalFilename();
            File destination = new File(filePath);
            destination.getParentFile().mkdirs();
            file.transferTo(destination);

            // Save file metadata to the database
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setFileSize(fileSize);
            fileEntity.setFilePath(filePath);
            fileEntity.setUploadedAt(LocalDateTime.now());
            fileEntity.setUploadedBy(user);
            fileRepository.save(fileEntity);

            // Update the user's storage usage
            user.setCurrentStorage(currentStorage + fileSize);
            userRepository.save(user);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    // Handle file deletion logic
    public ResponseEntity<String> deleteFile(Long fileId, User user) {
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);

        if (fileOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        FileEntity fileEntity = fileOptional.get();

        // Ensure the logged-in user is the owner
        if (!fileEntity.getUploadedBy().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this file.");
        }

        // Delete the file from the file system
        File storedFile = new File(fileEntity.getFilePath());
        if (storedFile.exists()) {
            storedFile.delete();
        }

        // Remove file metadata from the database
        fileRepository.delete(fileEntity);

        // Update the user's storage usage
        user.setCurrentStorage(user.getCurrentStorage() - fileEntity.getFileSize());
        userRepository.save(user);

        return ResponseEntity.ok("File deleted successfully.");
    }

    // List files for a user
    public List<FileEntity> getFiles(User user) {
        return fileRepository.findByUploadedByUsername(user.getUsername());
    }
}
