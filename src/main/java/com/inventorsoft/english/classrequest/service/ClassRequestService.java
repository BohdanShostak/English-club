package com.inventorsoft.english.classrequest.service;


import com.inventorsoft.english.classrequest.domain.dto.ClassRequestDto;
import com.inventorsoft.english.classrequest.domain.mapper.ClassRequestMapper;
import com.inventorsoft.english.classrequest.domain.model.ClassRequest;
import com.inventorsoft.english.classrequest.repository.ClassRequestRepository;
import com.inventorsoft.english.general.principal.CurrentUserProvider;
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
@Slf4j
@RequiredArgsConstructor
public class ClassRequestService extends UserRequestService<ClassRequestDto> {

    private final ClassRequestRepository classRequestRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public ClassRequestDto findById(Long id) {
        if (isUserAdmin()) {
            log.info("Finding ClassRequest by {} id...", id);
            return ClassRequestMapper.INSTANCE.mapModelToDto(classRequestRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Class request with id " + id + " not found")));
        }
        return ClassRequestMapper.INSTANCE.mapModelToDto(classRequestRepository.findByIdAndUserId(id,
                        currentUserProvider.getCurrentUser().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Class request with id " + id + " not found")));
    }

    @Override
    @Transactional
    public List<ClassRequestDto> getAll() {
        if (isUserAdmin()) {
            log.info("Get all ClassRequests");
            return ClassRequestMapper
                    .INSTANCE
                    .mapListOfClassRequestToListOfDto(classRequestRepository.findAll());
        }
        return allUserRequests(currentUserProvider.getCurrentUser().getId());
    }

    @Override
    @Transactional
    public void deleteRequestById(Long id) {
        classRequestRepository.deleteById(id);
        log.info("ClassRequest with id {} was deleted", id);
    }

    @Override
    @Transactional
    public List<ClassRequestDto> allUserRequests(Long id) {
        log.info("Getting all ClassRequests where userId {}", id);
        return ClassRequestMapper.INSTANCE.mapListOfClassRequestToListOfDto(classRequestRepository.findAllByUserId(id));
    }

    @Override
    @Transactional
    public ClassRequestDto rejectRequest(Long id) {
        ClassRequest classRequest = classRequestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Class request with id " + id + " not found"));
        classRequest.setStatus(RequestStatus.REJECTED);
        classRequest.setMeetingDate(null);
        classRequest.setMeetingUrl(null);
        classRequestRepository.save(classRequest);
        log.info("ClassRequest with id {} rejected", id);
        return ClassRequestMapper.INSTANCE.mapModelToDto(classRequest);
    }

    @Override
    @Transactional
    public ClassRequestDto createRequest(ClassRequestDto dto) {
        log.info("Creating ClassRequest");
        ClassRequest classRequest = ClassRequestMapper.INSTANCE.mapDtoToModel(dto);
        classRequest.setUser(userRepository.getReferenceById(currentUserProvider.getCurrentUser().getId()));
        classRequest.setStatus(RequestStatus.NEW);
        classRequestRepository.save(classRequest);
        log.info("ClassRequest successfully created");
        return ClassRequestMapper.INSTANCE.mapModelToDto(classRequest);
    }

    @Override
    @Transactional
    public ClassRequestDto confirmRequest(Long id, ClassRequestDto dto) {
        String currentUserEmail = currentUserProvider.getCurrentUser().getEmail();
        ClassRequest classRequest = classRequestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Class request with id " + id + " not found"));

        log.info("Confirming ClassRequest with {} id...", id);
        classRequest.setStatus(RequestStatus.APPROVED);
        ClassRequestMapper.INSTANCE.updateClassRequestFromDto(classRequest, dto);
        ClassRequest updatedClassRequest = classRequestRepository.save(classRequest);
        log.info("ClassRequest with id {} successfully confirmed", updatedClassRequest.getId());

        String firstName = updatedClassRequest.getUser().getFirstName();
        LocalDateTime meetingDate = updatedClassRequest.getMeetingDate();
        String meetingURL = updatedClassRequest.getMeetingUrl();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(currentUserEmail);
        mailMessage.setSubject(String.format("%s, your class request is approved!", firstName));
        mailMessage.setText(String.format("""
                Hello %s! Your class request is approved! 
                Meeting date and time: %s 
                Meeting URL: %s
                """, firstName, meetingDate, meetingURL));
        emailService.sendEmail(mailMessage);

        return ClassRequestMapper.INSTANCE.mapModelToDto(classRequest);
    }

    private boolean isUserAdmin() {
        return currentUserProvider.getCurrentUser().getRole().compareTo(Role.ROLE_ADMIN) == 0;
    }

}
