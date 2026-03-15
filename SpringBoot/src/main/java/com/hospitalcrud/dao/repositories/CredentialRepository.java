package com.hospitalcrud.dao.repositories;

import com.hospitalcrud.dao.model.Credential;

import java.util.List;

public interface CredentialRepository {
     List<Credential> getAll();
}
