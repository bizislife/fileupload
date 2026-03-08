package com.example.fileupload.controller;

import com.example.fileupload.model.UploadedFile;
import com.example.fileupload.repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;

import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FileController {
    private final UploadedFileRepository fileRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            List<UploadedFile> savedFiles = java.util.Arrays.stream(files).map(file -> {
                try {
                    Tika tika = new Tika();
                    String detectedType = tika.detect(file.getInputStream());
                    UploadedFile uploadedFile = UploadedFile.builder().fileName(file.getOriginalFilename())
                            .fileType(detectedType).fileSize(file.getSize()).uploadTime(LocalDateTime.now())
                            .data(file.getBytes()).build();
                    return fileRepository.save(uploadedFile);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            return ResponseEntity.ok(savedFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @PostMapping("/uploadSingle")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile file) {
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getInputStream());
            UploadedFile uploadedFile = UploadedFile.builder().fileName(file.getOriginalFilename())
                    .fileType(detectedType).fileSize(file.getSize()).uploadTime(LocalDateTime.now())
                    .data(file.getBytes()).build();
            UploadedFile savedFile = fileRepository.save(uploadedFile);
            savedFile.setData(null);
            return ResponseEntity.ok(savedFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping
    public List<UploadedFile> listFiles() {
        return fileRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        Optional<UploadedFile> file = fileRepository.findById(id);
        if (file.isPresent()) {
            fileRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
