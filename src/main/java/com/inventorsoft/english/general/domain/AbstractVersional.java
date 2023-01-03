package com.inventorsoft.english.general.domain;

import com.inventorsoft.english.security.user_details.UserDetailsImpl;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractVersional extends AbstractIdentifiable {

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private Long createdBy;

    @Column
    private Long updatedBy;


    @PrePersist
    private void prePersist() {
        Long principal = getPrincipalId();
        this.createdBy = principal;
        this.updatedBy = principal;
        this.createdAt = LocalDateTime.now();
    }

    private Long getPrincipalId() {
        Long id = null;
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (nonNull(authentication) && authentication.getPrincipal() instanceof UserDetailsImpl securityUser) {
            id = securityUser.getId();
        }
        return id;
    }
}