package com.hospitalcrud.ui;

import com.hospitalcrud.domain.model.CredentialUI;
import com.hospitalcrud.domain.services.CredentialService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestCredentials {

    private CredentialService credentialService;

    public RestCredentials(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/login")
    public boolean getCredential(@RequestBody CredentialUI credentialui){
        return  credentialService.get(credentialui);
    }
}
