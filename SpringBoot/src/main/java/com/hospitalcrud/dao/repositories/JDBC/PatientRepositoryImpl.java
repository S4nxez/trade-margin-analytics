package com.hospitalcrud.dao.repositories.JDBC;

import com.hospitalcrud.dao.mappers.JDBC.PatientRowMapperJDBC;
import com.hospitalcrud.dao.model.Patient;
import com.hospitalcrud.dao.repositories.JDBC.common.PoolDBConnection;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.PatientRepository;
import com.hospitalcrud.domain.errors.DuplicatedUserError;
import com.hospitalcrud.domain.errors.ForeignKeyConstraintError;
import com.hospitalcrud.utils.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Slf4j
@Repository
@Profile("jdbc")
public class PatientRepositoryImpl implements PatientRepository {

    private final PoolDBConnection pool;
    private final PatientRowMapperJDBC patientRowMapper;

    public PatientRepositoryImpl(PoolDBConnection poolDbConnection,
                                 PatientRowMapperJDBC patientRowMapper) {
        this.pool = poolDbConnection;
        this.patientRowMapper = patientRowMapper;
    }

    @Override
    public List<Patient> getAll() {
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(QuerysSQL.SELECT_ALL_PATIENTS);
            return patientRowMapper.mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(Patient patient) {
        try (Connection connection = pool.getConnection()) {
            connection.setAutoCommit(false);
            int generatedPatientId;

            try (PreparedStatement patientStatement = connection.prepareStatement(
                    QuerysSQL.INSERT_INTO_PATIENTS_NAME_DATE_OF_BIRTH_PHONE_VALUES,
                    Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement credentialStatement = connection.prepareStatement(
                         QuerysSQL.INSERT_INTO_CREDENTIALS_USERNAME_PASSWORD_VALUES)) {

                patientStatement.setString(1, patient.getName());
                patientStatement.setDate(2, Date.valueOf(patient.getBirthDate()));
                patientStatement.setString(3, patient.getPhone());
                patientStatement.executeUpdate();



                try (ResultSet generatedKeys = patientStatement.getGeneratedKeys()) {
                        generatedKeys.next();
                        generatedPatientId = generatedKeys.getInt(1);

                }

                if (patient.getCredential() != null) {
                    credentialStatement.setString(1, patient.getCredential().getUsername());
                    credentialStatement.setString(2, patient.getCredential().getPassword());
                    credentialStatement.setInt(3, generatedPatientId);
                    credentialStatement.executeUpdate();
                }

                connection.commit();
                return generatedPatientId;

            } catch (SQLIntegrityConstraintViolationException e) {
                try {
                    if (connection != null) connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                throw new DuplicatedUserError("Taken username");
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error while inserting patient and/or its credentials", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Patient patientDatabase) {
        try (Connection con = pool.getConnection();
                 PreparedStatement preparedStatement = con
                         .prepareStatement(QuerysSQL.UPDATE_PATIENT)) {
            preparedStatement.setString(1, patientDatabase.getName());
            preparedStatement.setDate(2,
                    java.sql.Date.valueOf(patientDatabase.getBirthDate()));
            preparedStatement.setString(3, patientDatabase.getPhone());
            preparedStatement.setInt(4, patientDatabase.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            throw new RuntimeException("SQL error",sqle);
        }
    }

    @Override
    public void delete(int id, boolean confirm) {
        if (!confirm) {
            throw new IllegalArgumentException(Constantes.DELETE_OPERATION_NOT_CONFIRMED);
        }

        try (Connection con = pool.getConnection()) {
            con.setAutoCommit(false);

            try (
                    PreparedStatement pstmtAppointment = con.prepareStatement(QuerysSQL.DELETE_APPOINTMENT);
                    PreparedStatement pstmtMedRecord = con.prepareStatement(QuerysSQL.DELETE_MED_RECORDS_BY_PATIENT);
                    PreparedStatement pstmtPayment = con.prepareStatement(QuerysSQL.DELETE_PAYMENT);
                    PreparedStatement pstmtPrescribedMed = con.prepareStatement(QuerysSQL.DELETE_PRESCRIBEDMEDICATION); //todo HACER UN JOIN PORQUE AQUI SOLO LE ESTOY PASANDO UN ID
                    PreparedStatement pstmtUserLogin = con.prepareStatement(QuerysSQL.DELETE_USER_LOGIN);
                    PreparedStatement pstmtPatient = con.prepareStatement(QuerysSQL.DELETE_PATIENT)
            ) {
                pstmtAppointment.setInt(1, id);
                pstmtMedRecord.setInt(1, id);
                pstmtPayment.setInt(1, id);
                pstmtPrescribedMed.setInt(1, id);

                pstmtAppointment.executeUpdate();
                pstmtMedRecord.executeUpdate();
                pstmtPayment.executeUpdate();
                pstmtPrescribedMed.executeUpdate();

                pstmtUserLogin.setInt(1, id);
                pstmtPatient.setInt(1, id);

                pstmtUserLogin.executeUpdate();
                pstmtPatient.executeUpdate();

                con.commit();
            } catch (SQLIntegrityConstraintViolationException e) {
                con.rollback();
                throw new ForeignKeyConstraintError("Unable to delete because another element depends on it");
                //TODO log.error("ERROR"); esto si el user tiene datos tiene que mandar un mensaje de que si efectivamente quiere borrarlos y si dice que si y manda el confirm a true entonces borrar
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Error while deleting", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Conexion error.", e);
        }
    }

}
