package com.inventorsoft.english.security.jwt;

import com.inventorsoft.english.security.user_details.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("${spring.security.jwt.access_token_duration}")
    private long accessTokenExpireDuration;

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired");
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace");
        } catch (MalformedJwtException ex) {
            log.error("JWT is malformed");
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported");
        } catch (SignatureException ex) {
            log.error("Signature validation failed");
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(UserDetailsImpl user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .claim("roles", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireDuration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}

