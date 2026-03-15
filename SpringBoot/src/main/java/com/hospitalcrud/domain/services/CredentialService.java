package com.hospitalcrud.domain.services;

import com.hospitalcrud.dao.model.Credential;
import com.hospitalcrud.dao.repositories.CredentialRepository;
import com.hospitalcrud.domain.model.CredentialUI;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public boolean get(CredentialUI credentialui) {
        return credentialRepository.getAll().stream()
                .anyMatch(credential ->
                        credential.getPassword().equals(credentialui.getPassword()) &&
                                credential.getUsername().equals(credentialui.getUsername())
                );
    }
}
