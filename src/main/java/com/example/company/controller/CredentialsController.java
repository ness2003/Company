package com.example.company.controller;

import com.example.company.dto.CredentialsDTO;
import com.example.company.entity.Credentials;
import com.example.company.service.CredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credentials")
@AllArgsConstructor
public class CredentialsController {

    private final CredentialsService credentialsService;

    @PostMapping
    public ResponseEntity<Credentials> create(@RequestBody CredentialsDTO credentialsDTO) {
        return mappingResponseCredentials(credentialsService.create(credentialsDTO));
    }

    @GetMapping
    public ResponseEntity<List<Credentials>> readAll() {
        return mappingResponseListCredentials(credentialsService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credentials> readById(@PathVariable Long id) {
        return mappingResponseCredentials(credentialsService.readById(id));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Credentials> readByLogin(@PathVariable String login) {
        return mappingResponseCredentials(credentialsService.readByLogin(login));
    }

    @PutMapping
    public ResponseEntity<Credentials> update(@RequestBody Credentials credentials) {
        return mappingResponseCredentials(credentialsService.update(credentials));
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        credentialsService.delete(id);
        return HttpStatus.OK;
    }

    private ResponseEntity<Credentials> mappingResponseCredentials(Credentials credentials) {
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    private ResponseEntity<List<Credentials>> mappingResponseListCredentials(List<Credentials> credentialsList) {
        return new ResponseEntity<>(credentialsList, HttpStatus.OK);
    }
}

