package com.inventorsoft.english.scheduler.domain;

import com.inventorsoft.english.general.domain.AbstractVersional;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class AppointmentRule extends AbstractVersional {

    @ManyToOne
    @JoinColumn(name = "english_group_id")
    private EnglishGroup englishGroup;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated
    private Occurrence occurrence;
    
    @Transient
    private Set<DayOfWeek> daysSelected = new HashSet<>();
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thur;
    private boolean fri;
    private boolean sat;
    private boolean sun;
    private Duration duration;

    public Set<DayOfWeek> getDaysSelected() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        if (mon) {
            daysOfWeek.add(DayOfWeek.MONDAY);
        }
        if (tue) {
            daysOfWeek.add(DayOfWeek.TUESDAY);
        }
        if (wed) {
            daysOfWeek.add(DayOfWeek.WEDNESDAY);
        }
        if (thur) {
            daysOfWeek.add(DayOfWeek.THURSDAY);
        }
        if (fri) {
            daysOfWeek.add(DayOfWeek.FRIDAY);
        }
        if (sat) {
            daysOfWeek.add(DayOfWeek.SATURDAY);
        }
        if (sun) {
            daysOfWeek.add(DayOfWeek.SUNDAY);
        }
        return daysOfWeek;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
        daysSelected.add(DayOfWeek.MONDAY);
    }

    public void setTue(boolean tue) {
        this.tue = tue;
        daysSelected.add(DayOfWeek.THURSDAY);
    }

    public void setWed(boolean wed) {
        this.wed = wed;
        daysSelected.add(DayOfWeek.WEDNESDAY);
    }

    public void setThur(boolean thur) {
        this.thur = thur;
        daysSelected.add(DayOfWeek.THURSDAY);
    }

    public void setFri(boolean fri) {
        this.fri = fri;
        daysSelected.add(DayOfWeek.FRIDAY);
    }

    public void setSat(boolean sat) {
        this.sat = sat;
        daysSelected.add(DayOfWeek.SATURDAY);
    }

    public void setSun(boolean sun) {
        this.sun = sun;
        daysSelected.add(DayOfWeek.SUNDAY);
    }
}
