package com.inventorsoft.english.users.controller;

import com.inventorsoft.english.users.domain.dto.UserRegistrationDto;
import com.inventorsoft.english.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/register")
public class UserRegistrationRestController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.registerUser(userRegistrationDto);
    }

    @GetMapping("/confirmation")
    public void confirmUserAccount(@RequestParam("token") String confirmationToken) {
        userService.confirmUser(confirmationToken);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void reRegisterUser(@RequestParam String userEmail) {
        userService.reRegisterUser(userEmail);
    }

}
