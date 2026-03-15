package com.hospitalcrud.dao.repositories.spring;


import com.hospitalcrud.dao.mappers.spring.CredentialRowMapper;
import com.hospitalcrud.dao.model.Credential;
import com.hospitalcrud.dao.repositories.CredentialRepository;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring")
public class CredentialRepositoryImpl implements CredentialRepository {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    private CredentialRowMapper credentialRowMapperSpring;

    @Override
    public List<Credential> getAll() {
        return jdbcClient.sql(QuerysSQL.GET_ALL_CREDENTIALS)
                .query(credentialRowMapperSpring)
                .list();
    }

    public void add(Credential credential) {
        jdbcClient.sql(QuerysSQL.INSERT_INTO_CREDENTIALS_USERNAME_PASSWORD_VALUES)
                .param(1, credential.getUsername())
                .param(2, credential.getPassword())
                .param(3, credential.getPatientId())
                .update();
    }

    public void delete(int id) {
        jdbcClient.sql(QuerysSQL.DELETE_USER_LOGIN)
                .param(1,id)
                .update();
    }
}
