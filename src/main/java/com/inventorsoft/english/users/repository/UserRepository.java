package com.inventorsoft.english.users.repository;

import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.users.domain.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {




    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByConfirmationToken(String confirmationToken);

    Page<User> findAllByEnglishGroupId(Long groupId, Pageable pageable);

    List<User> findUserByEnglishGroup(EnglishGroup englishGroup);

}
