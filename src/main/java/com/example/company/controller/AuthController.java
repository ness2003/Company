package com.example.company.controller;

import com.example.company.dto.LoginRequest;
import com.example.company.entity.User;
import com.example.company.repository.UserRepository;
import com.example.company.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private final UserService userService;
    private final String secretKey = "super_secret_key_12345678901234567890";


    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Received login request with email: {}", request.getEmail());

        User user = userService.findByEmail(request.getEmail());

        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = generateToken(user);
        return new LoginResponse(token, user.getRole().getName().name(), user.getId());
    }


    private String generateToken(User user) {
        SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());
        SecretKey secretKey2 = Keys.hmacShaKeyFor(this.secretKey.getBytes());
        System.out.println(secretKey);
        System.out.println(secretKey2);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().getName().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey)
                .compact();
    }
}


class LoginResponse {
    private String token;
    private String role;
    private Long userId;  // Добавляем ID пользователя

    public LoginResponse(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getUserId() { return userId; }
}

