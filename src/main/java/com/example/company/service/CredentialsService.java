package com.example.company.service;

import com.example.company.dto.CredentialsDTO;
import com.example.company.entity.Credentials;
import com.example.company.repository.CredentialsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final UserService userService;

    public Credentials create(CredentialsDTO credentialsDTO) {
        System.out.println("Создание учетных данных:");
        System.out.println("Login: " + credentialsDTO.getLogin());
        System.out.println("UserId: " + credentialsDTO.getUserId());

        Credentials credentials = Credentials.builder()
                .login(credentialsDTO.getLogin())
                .password(credentialsDTO.getPassword())
                .user(userService.readById(credentialsDTO.getUserId()))
                .build();

        System.out.println("Учетные данные перед сохранением:");
        System.out.println("Login: " + credentials.getLogin());
        System.out.println("User: " + (credentials.getUser() != null ? credentials.getUser().getEmail() : "Не указан"));

        return credentialsRepository.save(credentials);
    }

    public List<Credentials> readAll() {
        return credentialsRepository.findAll();
    }

    public Credentials readById(Long id) {
        return credentialsRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Credentials not found - " + id));
    }

    public Credentials readByLogin(String login) {
        return credentialsRepository.findByLogin(login).orElseThrow(() ->
                new RuntimeException("Credentials not found for login - " + login));
    }

    public Credentials update(Credentials credentials) {
        credentials.setUser(userService.readById(credentials.getUser().getId()));
        return credentialsRepository.save(credentials);
    }

    public void delete(Long id) {
        credentialsRepository.deleteById(id);
    }
}

