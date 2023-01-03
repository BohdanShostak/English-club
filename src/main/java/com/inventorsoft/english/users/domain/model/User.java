package com.inventorsoft.english.users.domain.model;

import com.inventorsoft.english.general.domain.AbstractVersional;
import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_confirmationtoken", columnList = "confirmationToken")
})
public class User extends AbstractVersional {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "englishGroup_id", referencedColumnName = "id")
    private EnglishGroup englishGroup;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isEnabled;

    private String confirmationToken;

    private LocalDateTime expiresAt;

    private EnglishLevel englishLevel;

    private String userInfo;

    private Long avatarId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    public User() {
        confirmationToken = UUID.randomUUID().toString();
    }
}
