package com.inventorsoft.english.security.jwt;

import com.inventorsoft.english.security.user_details.UserDetailsImpl;
import com.inventorsoft.english.users.domain.model.Role;
import com.inventorsoft.english.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!hasAuthorizationBearer(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(header);

        UserDetailsImpl userDetails = (UserDetailsImpl) getUserDetails(token);
        if (isUserDeletedCheck(userDetails)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!jwtService.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(String header) {
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    private String getAccessToken(String header) {
        return header.split(" ")[1].trim();
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        Claims claims = jwtService.parseClaims(token);

        String claimRoles = (String) claims.get("roles");

        userDetails.setRole(Role.valueOf(claimRoles));

        String subject = (String) claims.get(Claims.SUBJECT);
        String[] jwtSubject = subject.split(",");

        userDetails.setId(Long.parseLong(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);

        return userDetails;
    }

    private Boolean isUserDeletedCheck(UserDetailsImpl userDetails){
        return userRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new).getDeleted();
    }
}

