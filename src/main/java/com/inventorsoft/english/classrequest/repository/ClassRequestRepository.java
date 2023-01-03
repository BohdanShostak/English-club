package com.inventorsoft.english.classrequest.repository;

import com.inventorsoft.english.classrequest.domain.model.ClassRequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRequestRepository extends JpaRepository<ClassRequest, Long> {

    List<ClassRequest> findAllByUserId(long userId);

    Optional<ClassRequest> findByIdAndUserId(Long classRequestId, Long userId);
}
