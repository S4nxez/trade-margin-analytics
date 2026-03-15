package com.hospitalcrud.dao.repositories.spring;

import com.hospitalcrud.dao.mappers.spring.PaymentRowMapper;
import com.hospitalcrud.dao.model.Payment;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring")
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private PaymentRowMapper paymentRowMapperSpring;
    @Autowired
    JdbcClient jdbcClient;

    @Override
    public List<Payment> getAll() {
        return jdbcClient.sql(QuerysSQL.GET_ALL_PAYMENTS)
                .query(paymentRowMapperSpring)
                .list();
    }

    public void delete(int id) {
        jdbcClient.sql(QuerysSQL.DELETE_PAYMENT)
                .param(1,id)
                .update();
    }
}
