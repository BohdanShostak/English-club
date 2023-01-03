package com.inventorsoft.english.classrequest.controller;

import com.inventorsoft.english.classrequest.domain.dto.ClassRequestDto;
import com.inventorsoft.english.classrequest.service.ClassRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/class-requests")
@RequiredArgsConstructor
@Slf4j
public class ClassRequestRestController {

    private final ClassRequestService classRequestService;

    @GetMapping
    public List<ClassRequestDto> getAllClassRequests() {
        log.info("getAllClassRequests");
        return classRequestService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public List<ClassRequestDto> getAllClassRequestsByUser(@PathVariable long id) {
        log.info("getAllClassRequestsByUser");
        return classRequestService.allUserRequests(id);
    }

    @GetMapping("/{id}")
    public ClassRequestDto getClassRequestById(@PathVariable long id) {
        log.info("getClassRequest by id {}", id);
        return classRequestService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClassRequestDto createClassRequest(@Valid @RequestBody ClassRequestDto classRequestDto) {
        log.info("createClassRequest");
        return classRequestService.createRequest(classRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteClassRequest(@PathVariable Long id) {
        log.info("deleteClassRequest by id {}", id);
        classRequestService.deleteRequestById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/confirm/{id}")
    public ClassRequestDto confirmClassRequest(@PathVariable Long id, @Valid @RequestBody ClassRequestDto classRequestDto) {
        log.info("confirmClassRequest by id {}", id);
        return classRequestService.confirmRequest(id, classRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reject/{id}")
    public ClassRequestDto rejectClassRequest(@PathVariable Long id) {
        log.info("rejectClassRequest by id {}", id);
        return classRequestService.rejectRequest(id);
    }

}
