package com.inventorsoft.english.groups.domain.model;

import com.inventorsoft.english.general.domain.AbstractVersional;
import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "english_group")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnglishGroup extends AbstractVersional {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "englishGroup")
    private List<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private User mentor;

    @Column(name = "english_level")
    @Enumerated(EnumType.STRING)
    private EnglishLevel englishLevel;

    @OneToOne(fetch = FetchType.LAZY)
    private AppointmentRule rule;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
