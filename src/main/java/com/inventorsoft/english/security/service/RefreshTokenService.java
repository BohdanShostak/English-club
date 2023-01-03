package com.inventorsoft.english.security.service;

import com.inventorsoft.english.security.entity.AuthResponse;
import com.inventorsoft.english.security.jwt.JwtService;
import com.inventorsoft.english.security.refresh_token.RefreshToken;
import com.inventorsoft.english.security.repository.RefreshTokenRepository;
import com.inventorsoft.english.security.user_details.UserDetailsImpl;
import com.inventorsoft.english.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${spring.security.jwt.refresh_token_duration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUserId(long id) {
        return refreshTokenRepository.findByUserId(id);
    }

    private RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken updateRefreshToken(Long userId) {
        if (findByUserId(userId).isPresent()) {
            RefreshToken refreshToken = findByUserId(userId).get();

            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        return createRefreshToken(userId);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public ResponseEntity<?> refreshToken(String requestRefreshToken) {
        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    RefreshToken refreshToken = this.updateRefreshToken(user.getId());
                    String accessToken = jwtService.generateAccessToken(UserDetailsImpl.build(user));
                    return ResponseEntity.ok(new AuthResponse(user.getEmail(), accessToken, refreshToken.getToken()));
                })
                .orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }
}

