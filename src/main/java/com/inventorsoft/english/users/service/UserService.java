package com.inventorsoft.english.users.service;

import com.inventorsoft.english.general.principal.CurrentUserProvider;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.security.repository.RefreshTokenRepository;
import com.inventorsoft.english.users.domain.dto.UserDto;
import com.inventorsoft.english.users.domain.dto.UserRegistrationDto;
import com.inventorsoft.english.users.domain.mapper.UserMapper;
import com.inventorsoft.english.users.domain.model.Role;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final CurrentUserProvider currentUserProvider;

    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${server.domain}")
    private String domain;

    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long id) {
        return userMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("User with id= " + id + " not found!")));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserDto update(Long id, UserDto dto) {
        if (!currentUserProvider.getCurrentUser().getRole().equals(Role.ROLE_ADMIN) &&
                currentUserProvider.getCurrentUser().getId() != id) {
            throw new RuntimeException("No permission to update!");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        userMapper.update(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("pezda");
            return;
        }
        User user = userOptional.get();
        refreshTokenRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
    }

    @Transactional
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();

        if (userRepository.findByEmailIgnoreCase(userRegistrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already used!");
        }

        userMapper.update(userRegistrationDto, user);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        userMapper.toDto(userRepository.save(user));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        UriComponents builder =
                UriComponentsBuilder.fromHttpUrl(domain)
                        .pathSegment("api", "public", "register", "confirmation")
                        .queryParam("token", user.getConfirmationToken())
                        .build();
        mailMessage.setText("To confirm your account, please click here : "
                + builder);
        emailService.sendEmail(mailMessage);
    }

    @Transactional
    public void reRegisterUser(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("User with email " + userEmail + " not found!"));
        user.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        userMapper.toDto(userRepository.save(user));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Re-registration!");
        UriComponents builder =
                UriComponentsBuilder.fromHttpUrl(domain)
                        .pathSegment("api", "public", "register", "confirmation")
                        .queryParam("token", user.getConfirmationToken())
                        .build();
        mailMessage.setText("To confirm your account, please click here : "
                + builder);
        emailService.sendEmail(mailMessage);
    }

    @SneakyThrows
    @Transactional
    public void confirmUser(String confirmToken) {
        User user = userRepository.findByConfirmationToken(confirmToken)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        LocalDateTime expiredAt = user.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllByGroup(Long id, Pageable pageable) {
        return userRepository.findAllByEnglishGroupId(id, pageable).map(userMapper::toDto);
    }

    @Transactional
    public List<User> findUsersByEnglishGroup(EnglishGroup englishGroup) {
        return userRepository.findUserByEnglishGroup(englishGroup);
    }

}
