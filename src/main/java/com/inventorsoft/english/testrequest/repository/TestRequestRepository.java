package com.inventorsoft.english.testrequest.repository;

import com.inventorsoft.english.testrequest.domain.model.TestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TestRequestRepository extends JpaRepository<TestRequest, Long> {
    List<TestRequest> findAllByUserId(Long id);
    Optional<TestRequest> findByIdAndUserId(Long testRequestId, Long userId);
}
