package com.inventorsoft.english.googlestorage.controller;

import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.googlestorage.service.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileRestController {

    private final FilesService filesService;

    @PostMapping
    @SneakyThrows
    public void upload(@RequestParam String fileName, @RequestParam FileType fileType, @RequestParam("file") MultipartFile file) {
        filesService.uploadFile(fileName, file.getBytes(), fileType);
    }

    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam String fileName, @RequestParam FileType fileType) {
        byte[] file = filesService.downloadFile(fileName, fileType);
        ByteArrayResource resource = new ByteArrayResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(file.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
