package com.hospitalcrud.dao.repositories.JDBC;

import com.hospitalcrud.dao.mappers.JDBC.MedRecordRowMapperJDBC;
import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.repositories.JDBC.common.PoolDBConnection;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.MedRecordRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@Profile("jdbc")
public class MedRecordRepositoryImpl implements MedRecordRepository {

    private final MedicationRepositoryImpl medicationRepositoryImpl;
    private PoolDBConnection pool;
    private MedRecordRowMapperJDBC rowMapper;

    public MedRecordRepositoryImpl(PoolDBConnection pool, MedRecordRowMapperJDBC rowMapper, MedicationRepositoryImpl medicationRepositoryImpl) {
        this.pool = pool;
        this.rowMapper = rowMapper;
        this.medicationRepositoryImpl = medicationRepositoryImpl;
    }

    @Override
    public List<MedRecord> getAll(int idPatient) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(QuerysSQL.SELECT_ALL_MEDRECORDS)) {

            statement.setInt(1, idPatient);
            ResultSet rs = statement.executeQuery();
            return rowMapper.mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(MedRecord medRecord) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     QuerysSQL.INSERT_INTO_MEDICAL_RECORDS,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, medRecord.getIdPatient());
            preparedStatement.setInt(2, medRecord.getIdDoctor());
            preparedStatement.setString(3, medRecord.getDiagnosis());
            preparedStatement.setDate(4, Date.valueOf(medRecord.getDate()));
            medRecord.getMedications().forEach(medication -> {
                medicationRepositoryImpl.add(medication);
            });
            if (preparedStatement.executeUpdate() == 0)
                throw new SQLException("Creating error, no rows affected");

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getInt(1);
            else
                throw new SQLException("Error generating id");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding medrecord", e);
        }
    }


    @Override
    public void delete(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(QuerysSQL.DELETE_FROM_MED_RECORD)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    public void deleteByIdPatient(int idPatient) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(QuerysSQL.DELETE_MED_RECORDS_BY_PATIENT)) {

            preparedStatement.setInt(1, idPatient);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ", e);
        }
    }

    @Override
    public void update(MedRecord medRecord) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE medical_records SET patient_id = ?, doctor_id = ?, diagnosis = ?, admission_date = ? WHERE record_id = ?")) {

            preparedStatement.setInt(1, medRecord.getIdPatient());
            preparedStatement.setInt(2, medRecord.getIdDoctor());
            preparedStatement.setString(3, medRecord.getDiagnosis());
            preparedStatement.setDate(4, Date.valueOf(medRecord.getDate()));
            preparedStatement.setInt(5, medRecord.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating medrecord", e);
        }
    }
}
