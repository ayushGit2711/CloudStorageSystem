package com.ayushspring.cloudstoragesystem.controller;

import com.ayushspring.cloudstoragesystem.model.FileEntity;
import com.ayushspring.cloudstoragesystem.model.User;
import com.ayushspring.cloudstoragesystem.repository.UserRepository;
import com.ayushspring.cloudstoragesystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to upload a new file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String username) {
        // Retrieve the user based on the provided username
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Delegate the file upload to the service
        return fileService.uploadFile(file, user);
    }

    // Endpoint to list files uploaded by the user
    @GetMapping
    public ResponseEntity<List<FileEntity>> getFiles(@RequestParam String username) {
        // Retrieve the user based on the provided username
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Delegate to the service for listing files
        return ResponseEntity.ok(fileService.getFiles(user));
    }

    // Endpoint to delete a file by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, @RequestParam String username) {
        // Retrieve the user based on the provided username
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Delegate the deletion to the service
        return fileService.deleteFile(id, user);
    }
}
