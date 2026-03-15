package com.hospitalcrud.dao.repositories.spring;

import com.hospitalcrud.dao.model.Doctor;
import com.hospitalcrud.dao.repositories.DoctorRepository;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring")
public class DoctorRepositoryImpl implements DoctorRepository {

    @Autowired
    JdbcClient jdbcClient;

    @Override
    public List<Doctor> getAll() {
        return jdbcClient.sql(QuerysSQL.GET_ALL_DOCTORS)
                .query(Doctor.class)
                .list();
    }
}
