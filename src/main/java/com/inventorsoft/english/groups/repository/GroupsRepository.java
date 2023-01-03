package com.inventorsoft.english.groups.repository;

import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<EnglishGroup, Long> {

    List<EnglishGroup> findAllByMentorId(long mentorId);

}
