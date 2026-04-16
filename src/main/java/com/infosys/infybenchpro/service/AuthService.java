package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.dto.LoginRequest;
import com.infosys.infybenchpro.dto.LoginResponse;
import com.infosys.infybenchpro.entity.AppUser;
import com.infosys.infybenchpro.repository.AppUserRepository;
import com.infosys.infybenchpro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final AppUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtUtil.generateToken(
            user.getUsername(),
            user.getRole().name(),
            user.getEmployeeId()
        );

        return new LoginResponse(
            token,
            user.getUsername(),
            user.getRole().name(),
            user.getEmployeeId(),
            user.isRequiresPasswordChange()
        );
    }

    public void changePassword(String username, String newPassword) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setRequiresPasswordChange(false);
        userRepository.save(user);
    }
}
