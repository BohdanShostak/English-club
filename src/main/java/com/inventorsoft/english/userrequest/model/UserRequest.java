package com.inventorsoft.english.userrequest.model;

import com.inventorsoft.english.general.domain.AbstractVersional;
import com.inventorsoft.english.users.domain.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class UserRequest extends AbstractVersional {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    protected User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected RequestStatus status;

    @Column(name = "meeting_date")
    protected LocalDateTime meetingDate;

    @Column(name = "meeting_url")
    protected String meetingUrl;

    @Column(name = "notes")
    protected String notes;
}
