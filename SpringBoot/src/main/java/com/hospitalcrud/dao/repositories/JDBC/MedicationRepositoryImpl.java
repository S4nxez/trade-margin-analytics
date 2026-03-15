package com.hospitalcrud.dao.repositories.JDBC;

import com.hospitalcrud.dao.mappers.JDBC.MedicationRowMapperJDBC;
import com.hospitalcrud.dao.model.Medication;
import com.hospitalcrud.dao.repositories.JDBC.common.PoolDBConnection;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.MedicationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("jdbc")
public class MedicationRepositoryImpl implements MedicationRepository {
    private final PoolDBConnection pool;
    private final MedicationRowMapperJDBC rowMapper;

    public MedicationRepositoryImpl(PoolDBConnection pool, MedicationRowMapperJDBC rowMapper) {
        this.pool = pool;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Medication> getAll() {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(QuerysSQL.GET_ALL_MEDICATIONS)) {

            ResultSet rs = preparedStatement.executeQuery();
            return rowMapper.mapRow(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Error getting medications", e);
        }
    }

    public void add(Medication medication){
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(QuerysSQL.INSERT_INTO_MEDICATIONS)) {

            preparedStatement.setInt(1, medication.getMedRecordId());
            preparedStatement.setString(2, medication.getMedicationName());
            preparedStatement.setString(3, "0mg");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding medication", e);
        }

    }
}
