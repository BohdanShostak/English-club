package com.inventorsoft.english.lessons.repository;

import com.inventorsoft.english.lessons.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Page<Lesson> findAllByEnglishGroupId(Long groupId, Pageable pageable);

    Page<Lesson> findAllByStartTimeBetween(LocalDateTime periodBegin, LocalDateTime periodEnd,
                                           Pageable pageable);

    List<Lesson> findAllByEnglishGroupIdAndStartTimeBetween(Long groupId,
                                                            LocalDateTime startInterval,
                                                            LocalDateTime endInterval);

    List<Lesson> findAllByEnglishGroupIdAndStartTimeAfter(Long groupId,
                                                          LocalDateTime startInterval);
}
