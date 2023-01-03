package com.inventorsoft.english.uploadedfiles.repository;

import com.inventorsoft.english.uploadedfiles.domain.model.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadedFilesRepository extends JpaRepository<UploadedFile, Long> {

    Optional<UploadedFile> findByFileName(String fileName);

    Page<UploadedFile> findAllByLessonId(Long lessonId, Pageable pageable);

    Page<UploadedFile> findAllByLessonIsNull(Pageable pageable);

    void deleteAllByLessonId(long lessonId);

}
