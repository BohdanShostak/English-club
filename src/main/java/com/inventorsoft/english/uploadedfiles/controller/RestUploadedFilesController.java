package com.inventorsoft.english.uploadedfiles.controller;

import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.uploadedfiles.domain.dto.UploadedFileDto;
import com.inventorsoft.english.uploadedfiles.service.UploadedFilesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class RestUploadedFilesController {

    private final UploadedFilesService uploadedFilesService;

    @GetMapping("/{id}")
    public UploadedFileDto getUploadedFileById(@PathVariable Long id) {
        return uploadedFilesService.getUploadedFile(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadedUploadedFileById(@PathVariable Long id) {
        log.info("Get Uploaded File by id {}", id);
        byte[] file = uploadedFilesService.getUploadedFileContent(id);
        String fileName = uploadedFilesService.getUploadedFileFromDb(id).getFileName();
        ByteArrayResource resource = new ByteArrayResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(file.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + fileName)
                .body(resource);
    }

    @GetMapping("/name/{fileName}")
    public ResponseEntity<Resource> getUploadedFileByName(@PathVariable String fileName) {
        log.info("Get Uploaded File by name {}", fileName);
        byte[] file = uploadedFilesService.getUploadedFileByFileName(fileName);
        ByteArrayResource resource = new ByteArrayResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(file.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + fileName)
                .body(resource);
    }

    @SneakyThrows
    @GetMapping()
    public Page<UploadedFileDto> getCommonUploadedFiles(@PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Get All Common Uploaded Files");
        return uploadedFilesService.getCommonUploadedFiles(pageable);
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public void downloadCommonUploadedFiles(HttpServletResponse response) throws IOException {
        log.info("Download All Common Uploaded Files");
        uploadedFilesService.downloadCommonUploadedFiles(response);
    }

    @SneakyThrows
    @GetMapping("/lesson/{id}")
    public Page<UploadedFileDto> getAllUploadedFilesByLesson(
            @PathVariable long id, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Get All Uploaded Files By Lesson with id {}", id);
        return uploadedFilesService.getUploadedFilesByLesson(id, pageable);
    }

    @GetMapping(value = "/lesson/{id}/download", produces = "application/zip")
    public void downloadAllUploadedFilesByLesson(
            @PathVariable long id, HttpServletResponse response) throws IOException {
        log.info("Download All Uploaded Files By Lesson with id {}", id);
        uploadedFilesService.downloadUploadedFilesByLesson(response, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UploadedFileDto uploadFile(
            @RequestParam FileType fileType,
            @RequestParam("file") MultipartFile file) {
        log.info("Upload File");
        file.getName();
        return uploadedFilesService.uploadFile(file, fileType);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/lesson/{id}")
    public UploadedFileDto uploadFileForLesson(
            @PathVariable Long id,
            @RequestParam FileType fileType,
            @RequestParam("file") MultipartFile file) {
        log.info("Upload file for a lesson");
        return uploadedFilesService.uploadFileForLesson(id, file, fileType);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUploadedFiles(@PathVariable Long id) {
        log.info("Delete uploaded file by id {}", id);
        uploadedFilesService.deleteUploadedFile(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/lesson/{id}")
    public void deleteUploadedFilesByLesson(@PathVariable Long id) {
        log.info("Delete all uploaded files by lesson id {}", id);
        uploadedFilesService.deleteUploadedFilesByLesson(id);
    }

}
