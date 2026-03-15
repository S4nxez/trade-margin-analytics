package com.hospitalcrud.dao.repositories.JDBC;

import com.hospitalcrud.dao.mappers.JDBC.PaymentRowMapperJDBC;
import com.hospitalcrud.dao.model.Payment;
import com.hospitalcrud.dao.repositories.JDBC.common.PoolDBConnection;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.PaymentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Profile("jdbc")
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PoolDBConnection pool;
    private final PaymentRowMapperJDBC rowMapper;

    public PaymentRepositoryImpl(PoolDBConnection pool, PaymentRowMapperJDBC rowMapper) {
        this.pool = pool;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Payment> getAll() {
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(QuerysSQL.GET_ALL_PAYMENTS);
            return rowMapper.mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
