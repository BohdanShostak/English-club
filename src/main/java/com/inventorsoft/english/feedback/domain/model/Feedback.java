package com.inventorsoft.english.feedback.domain.model;

import javax.persistence.Entity;

import com.inventorsoft.english.general.domain.AbstractIdentifiable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Feedback extends AbstractIdentifiable {

    private String anonymousFeedback;

    private LocalDateTime createdAt;
}
