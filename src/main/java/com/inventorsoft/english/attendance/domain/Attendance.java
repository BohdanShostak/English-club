package com.inventorsoft.english.attendance.domain;


import com.inventorsoft.english.general.domain.AbstractVersional;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.users.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Attendance extends AbstractVersional {
     
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "lesson_id", nullable = false)
     private Lesson lesson;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     private Boolean isPresent;

     public Attendance(Lesson lesson, User user) {
          this.lesson = lesson;
          this.user = user;
     }
}
