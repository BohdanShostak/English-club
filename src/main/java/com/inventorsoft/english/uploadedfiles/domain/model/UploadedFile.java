package com.inventorsoft.english.uploadedfiles.domain.model;

import com.inventorsoft.english.general.domain.AbstractIdentifiable;
import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.users.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "uploaded_files")
@NoArgsConstructor
public class UploadedFile extends AbstractIdentifiable {

    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    FileType fileType;

    private LocalDateTime createdAt;

}
