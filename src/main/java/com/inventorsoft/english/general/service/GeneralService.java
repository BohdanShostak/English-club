package com.inventorsoft.english.general.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class GeneralService<ENTITY, Long> {

    CrudRepository<ENTITY, Long> repository;

    public Optional<ENTITY> findById(Long id) {
        return repository.findById(id);
    }

    public ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    public ENTITY getById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
