package com.inventorsoft.english.security.service;

import com.inventorsoft.english.security.entity.AuthRequest;
import com.inventorsoft.english.security.entity.AuthResponse;
import com.inventorsoft.english.security.jwt.JwtService;
import com.inventorsoft.english.security.refresh_token.RefreshToken;
import com.inventorsoft.english.security.user_details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsImplService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<?> auth(AuthRequest authRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.updateRefreshToken(userDetails.getId());
        AuthResponse authResponse = new AuthResponse(userDetails.getUsername(), accessToken, refreshToken.getToken());
        return ResponseEntity.ok().body(authResponse);
    }
}
