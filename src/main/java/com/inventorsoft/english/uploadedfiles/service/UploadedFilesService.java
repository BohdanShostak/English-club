package com.inventorsoft.english.uploadedfiles.service;

import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.googlestorage.model.Properties;
import com.inventorsoft.english.googlestorage.service.FilesService;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.repository.LessonRepository;
import com.inventorsoft.english.uploadedfiles.domain.dto.UploadedFileDto;
import com.inventorsoft.english.uploadedfiles.domain.mapper.UploadedFilesMapper;
import com.inventorsoft.english.uploadedfiles.domain.model.UploadedFile;
import com.inventorsoft.english.uploadedfiles.repository.UploadedFilesRepository;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class UploadedFilesService {

    private final UploadedFilesMapper mapper = UploadedFilesMapper.INSTANCE;

    private final UploadedFilesRepository uploadedFilesRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final FilesService filesService;

    private Map<FileType, String> fileTypeMap;
    private final Properties properties;

    @PostConstruct
    public void initMap() {
        fileTypeMap = Map.of(FileType.HOMEWORK, properties.getHomeWork(),
                FileType.IMAGE, properties.getImages());
    }

    @Transactional(readOnly = true)
    public UploadedFileDto getUploadedFileFromDb(long id) {
        UploadedFile uploadedFile = uploadedFilesRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Uploaded file with id= " + id + " not found!"));
        return UploadedFilesMapper.INSTANCE.convertToDto(uploadedFile);
    }

    @Transactional(readOnly = true)
    public byte[] getUploadedFileByFileName(String name) {
        log.info("Finding Uploaded File by {} name in db", name);
        UploadedFile uploadedFile = uploadedFilesRepository.findByFileName(name)
                .orElseThrow(() ->
                        new EntityNotFoundException("Uploaded file with name= " + name + " not found!"));
        log.info("Uploaded File with {} name is found", name);
        return filesService.downloadFile(uploadedFile.getFileName(), uploadedFile.getFileType());
    }

    @Transactional(readOnly = true)
    public UploadedFileDto getUploadedFile(Long id) {
        log.info("Finding Uploaded File by {} id in db", id);
        return uploadedFilesRepository.findById(id)
                .map(mapper::convertToDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Uploaded file with id= " + id + " not found!"));
    }

    @Transactional(readOnly = true)
    public byte[] getUploadedFileContent(long id) {
        log.info("Finding Uploaded File by {} id in db", id);
        UploadedFile uploadedFile = uploadedFilesRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Uploaded file with id= " + id + " not found!"));
        log.info("Uploaded File with {} id is found", id);
        return filesService.downloadFile(uploadedFile.getFileName(), uploadedFile.getFileType());
    }

    @Transactional(readOnly = true)
    public Page<UploadedFileDto> getCommonUploadedFiles(Pageable pageable) throws IOException {
        return uploadedFilesRepository.findAllByLessonIsNull(pageable)
                .map(mapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<UploadedFileDto> getUploadedFilesByLesson(Long lessonId, Pageable pageable) {
        return uploadedFilesRepository.findAllByLessonId(lessonId, pageable)
                .map(mapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public HttpServletResponse downloadCommonUploadedFiles(
            HttpServletResponse response) throws IOException {
        List<UploadedFile> dbFilesList = uploadedFilesRepository.findAllByLessonIsNull(Pageable.unpaged())
                .getContent();
        return downloadFiles(response, dbFilesList);
    }

    @Transactional(readOnly = true)
    public HttpServletResponse downloadUploadedFilesByLesson(
            HttpServletResponse response, Long lessonId) throws IOException {
        List<UploadedFile> dBFilesList = uploadedFilesRepository.findAllByLessonId(lessonId, Pageable.unpaged())
                .getContent();
        return downloadFiles(response, dBFilesList);
    }

    private HttpServletResponse downloadFiles(
            HttpServletResponse response, List<UploadedFile> dBFilesList) throws IOException {

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        ByteArrayResource resource;

        for (UploadedFile uploadedFile : dBFilesList) {
            Long commonDBFileId = uploadedFile.getId();
            byte[] currentFile = getUploadedFileContent(commonDBFileId);
            resource = new ByteArrayResource(currentFile);
            ZipEntry zipEntry = new ZipEntry(uploadedFile.getFileName());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllCommonFiles");
        return response;
    }

    @SneakyThrows
    @Transactional
    public UploadedFileDto uploadFile(MultipartFile file, FileType fileType) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailIgnoreCase(currentUserEmail)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with email= " + currentUserEmail + " not found!"));
        log.info("Start of file uploading ");

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setCreatedAt(LocalDateTime.now());
        uploadedFile.setCreatedBy(currentUser);
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setFileType(fileType);
        filesService.initMap();

        uploadedFile = uploadedFilesRepository.save(uploadedFile);
        filesService.uploadFile(file.getOriginalFilename(), file.getBytes(), fileType);
        log.info("File successfully uploaded");
        return UploadedFilesMapper.INSTANCE.convertToDto(uploadedFile);
    }

    @SneakyThrows
    @Transactional
    public UploadedFileDto uploadFileForLesson(Long lessonId, MultipartFile file, FileType fileType) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailIgnoreCase(currentUserEmail)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with email= " + currentUserEmail + " not found!"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson with id= " + lessonId + " not found!"));

        log.info("Start of file uploading ");
        UploadedFile uploadedFile = new UploadedFile();
        // uploadedFile.setCreatedAt(LocalDateTime.now());
        uploadedFile.setCreatedBy(currentUser);
        uploadedFile.setLesson(lesson);
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setFileType(fileType);

        uploadedFile = uploadedFilesRepository.save(uploadedFile);
        filesService.uploadFile(file.getOriginalFilename(), file.getBytes(), fileType);
        log.info("File successfully uploaded");
        return UploadedFilesMapper.INSTANCE.convertToDto(uploadedFile);
    }

    @Transactional
    public void unbindFilesFromLesson(Long lessonId) {
        List<UploadedFile> filesToUpdate = uploadedFilesRepository.findAllByLessonId(lessonId, Pageable.unpaged())
                .getContent();
        for (var file : filesToUpdate) {
            file.setLesson(null);
        }
        uploadedFilesRepository.saveAll(filesToUpdate);
    }

    @Transactional
    public void deleteUploadedFile(long id) {
        UploadedFile uploadedFile = uploadedFilesRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Uploaded file with id= " + id + " not found!"));
        boolean isDeleted = filesService.deleteFile(uploadedFile.getFileName(), uploadedFile.getFileType());
        uploadedFilesRepository.deleteById(id);
        log.info("File with id {} was deleted: {}", id, isDeleted);
    }

    @Transactional
    public void deleteUploadedFilesByLesson(long id) {
        List<UploadedFile> filesByLesson = uploadedFilesRepository.findAllByLessonId(id, Pageable.unpaged())
                .getContent();
        if (filesByLesson.isEmpty()) {
            return;
        }
        List<String> fileNames = new ArrayList<>();
        filesByLesson.forEach(file -> fileNames.add(file.getFileName()));

        uploadedFilesRepository.deleteAllByLessonId(id);
        filesService.deleteListFiles(fileNames, filesByLesson.get(0).getFileType());
        log.info("Files for lesson with id {} were deleted", id);
    }

}
