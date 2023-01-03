package com.inventorsoft.english.testrequest.controller;

import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.testrequest.domain.dto.TestRequestDto;
import com.inventorsoft.english.testrequest.service.TestRequestService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/test-requests")
public class RestTestRequestController {


    private final TestRequestService testRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestRequestDto registerRequest(@RequestBody @Valid TestRequestDto testRequestDto) {
        return testRequestService.createRequest(testRequestDto);
    }

    @GetMapping
    public List<TestRequestDto> getAll() {
        return testRequestService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public List<TestRequestDto> getAllByUser(@PathVariable Long id) {
        return testRequestService.allUserRequests(id);
    }

    @GetMapping("/{id}")
    public TestRequestDto getTestRequestById(@PathVariable Long id) {
        return testRequestService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTestRequest(@PathVariable Long id) {
        testRequestService.deleteRequestById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/confirm/{id}")
    public TestRequestDto confirmClassRequest(@PathVariable Long id, @Valid @RequestBody TestRequestDto testRequestDto) {
        return testRequestService.confirmRequest(id, testRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reject/{id}")
    public TestRequestDto rejectClassRequest(@PathVariable Long id) {
        return testRequestService.rejectRequest(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/complete/{id}")
    public TestRequestDto completeRequest(@PathVariable Long id, @Valid @RequestBody EnglishLevel englishLevel) {
        return testRequestService.completeTestRequest(id, englishLevel);
    }
}
