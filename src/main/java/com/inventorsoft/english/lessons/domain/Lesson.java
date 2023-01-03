package com.inventorsoft.english.lessons.domain;

import com.inventorsoft.english.general.domain.AbstractIdentifiable;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
public class Lesson extends AbstractIdentifiable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "english_group_id")
    private EnglishGroup englishGroup;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    public boolean equalsExceptId(Lesson l2) {
        return Objects.equals(englishGroup, l2.englishGroup)
                && Objects.equals(startTime, l2.startTime)
                && Objects.equals(endTime, l2.endTime);
    }
}
