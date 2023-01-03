package com.inventorsoft.english.users.controller;

import com.inventorsoft.english.security.entity.AuthRequest;
import com.inventorsoft.english.security.entity.TokenRefreshRequest;
import com.inventorsoft.english.security.service.RefreshTokenService;
import com.inventorsoft.english.security.service.UserDetailsImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class AuthController {


    private final UserDetailsImplService userService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        return userService.auth(authRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.refreshToken(requestRefreshToken);
    }
}
