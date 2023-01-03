package com.inventorsoft.english.testrequest.service;

import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.general.principal.CurrentUserProvider;
import com.inventorsoft.english.testrequest.domain.dto.TestRequestDto;
import com.inventorsoft.english.testrequest.domain.mapper.TestRequestMapper;
import com.inventorsoft.english.testrequest.domain.model.TestRequest;
import com.inventorsoft.english.testrequest.repository.TestRequestRepository;
import com.inventorsoft.english.userrequest.model.RequestStatus;
import com.inventorsoft.english.userrequest.service.UserRequestService;
import com.inventorsoft.english.users.domain.model.Role;
import com.inventorsoft.english.users.repository.UserRepository;
import com.inventorsoft.english.users.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TestRequestService extends UserRequestService<TestRequestDto> {

    private final TestRequestRepository testRequestRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public TestRequestDto findById(Long id) {
        if (isUserAdmin()) {
            log.info("Finding TestRequest by {} id...", id);
            return TestRequestMapper.INSTANCE.mapModelToDto(testRequestRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Test request with id " + id + " not found")));
        }
        return TestRequestMapper.INSTANCE.mapModelToDto(testRequestRepository.findByIdAndUserId(id, currentUserProvider.getCurrentUser().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Test request with id " + id + " not found")));
    }

    @Override
    @Transactional
    public List<TestRequestDto> getAll() {
        if (isUserAdmin()) {
            log.info("Get all TestRequests");
            return TestRequestMapper
                    .INSTANCE
                    .mapListOfTestRequestToListOfDto(testRequestRepository.findAll());
        }
        return allUserRequests(currentUserProvider.getCurrentUser().getId());
    }

    @Override
    @Transactional
    public void deleteRequestById(Long id) {
        testRequestRepository.deleteById(id);
        log.info("TestRequest with id {} was deleted", id);
    }

    @Override
    @Transactional
    public List<TestRequestDto> allUserRequests(Long id) {
        log.info("Getting all TestRequests where userId {}", id);
        return TestRequestMapper.INSTANCE.mapListOfTestRequestToListOfDto(testRequestRepository.findAllByUserId(id));
    }

    private boolean isUserAdmin() {
        return currentUserProvider.getCurrentUser().getRole().compareTo(Role.ROLE_ADMIN) == 0;
    }

    @Override
    @Transactional
    public TestRequestDto rejectRequest(Long id) {
        TestRequest testRequest = testRequestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Test request with id " + id + " not found"));
        testRequest.setStatus(RequestStatus.REJECTED);
        testRequest.setMeetingDate(null);
        testRequest.setMeetingUrl(null);
        testRequestRepository.save(testRequest);
        log.info("TestRequest with id {} rejected", id);
        return TestRequestMapper.INSTANCE.mapModelToDto(testRequest);
    }

    @Override
    @Transactional
    public TestRequestDto createRequest(TestRequestDto dto) {
        log.info("Creating TestRequest");
        TestRequest testRequest = TestRequestMapper.INSTANCE.mapDtoToModel(dto);
        testRequest.setUser(userRepository.getReferenceById(currentUserProvider.getCurrentUser().getId()));
        testRequest.setCreatedAt(LocalDateTime.now());
        testRequest.setStatus(RequestStatus.NEW);
        testRequestRepository.save(testRequest);
        log.info("TestRequest successfully created");
        return TestRequestMapper.INSTANCE.mapModelToDto(testRequest);
    }

    @Override
    @Transactional
    public TestRequestDto confirmRequest(Long id, TestRequestDto dto) {
        String currentUserEmail = currentUserProvider.getCurrentUser().getEmail();
        TestRequest testRequest = testRequestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Test request with id " + id + " not found"));

        log.info("Confirming TestRequest with {} id...", id);
        testRequest.setStatus(RequestStatus.APPROVED);
        TestRequestMapper.INSTANCE.updateTestRequestFromDto(testRequest, dto);
        TestRequest updatedTestRequest = testRequestRepository.save(testRequest);
        log.info("TestRequest with id {} successfully confirmed", updatedTestRequest.getId());

        String firstName = updatedTestRequest.getUser().getFirstName();
        LocalDateTime meetingDate = updatedTestRequest.getMeetingDate();
        String meetingURL = updatedTestRequest.getMeetingUrl();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(currentUserEmail);
        mailMessage.setSubject(String.format("%s, your test request is approved!", firstName));
        mailMessage.setText(String.format("""
                Hello %s! Your test request is approved! 
                Meeting date and time: %s 
                Meeting URL: %s
                """, firstName, meetingDate, meetingURL));
        emailService.sendEmail(mailMessage);

        return TestRequestMapper.INSTANCE.mapModelToDto(testRequest);
    }

    @Transactional
    public TestRequestDto completeTestRequest(Long id, EnglishLevel result) {
        TestRequest testRequest = testRequestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Test request with id " + id + " not found"));
        testRequest.setResult(result);
        testRequest.setStatus(RequestStatus.COMPLETED);
        return TestRequestMapper.INSTANCE.mapModelToDto(testRequest);
    }
}
