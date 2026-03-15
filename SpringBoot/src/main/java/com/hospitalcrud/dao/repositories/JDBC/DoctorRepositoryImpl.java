package com.hospitalcrud.dao.repositories.JDBC;

import com.hospitalcrud.dao.mappers.JDBC.DoctorRowMapperJDBC;
import com.hospitalcrud.dao.model.Doctor;
import com.hospitalcrud.dao.repositories.DoctorRepository;
import com.hospitalcrud.dao.repositories.JDBC.common.PoolDBConnection;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Profile("jdbc")
public class DoctorRepositoryImpl implements DoctorRepository {

    private PoolDBConnection pool;
    private DoctorRowMapperJDBC doctorRowMapperJDBC;

    public DoctorRepositoryImpl(PoolDBConnection pool, DoctorRowMapperJDBC doctorRowMapperJDBC) {
        this.pool = pool;
        this.doctorRowMapperJDBC = doctorRowMapperJDBC;
    }

    @Override
    public List<Doctor> getAll() {
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(QuerysSQL.GET_ALL_DOCTORS);

            return doctorRowMapperJDBC.mapRow(rs);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
