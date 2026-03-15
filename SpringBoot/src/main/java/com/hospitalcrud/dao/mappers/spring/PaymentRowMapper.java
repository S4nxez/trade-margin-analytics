package com.hospitalcrud.dao.mappers.spring;

import com.hospitalcrud.dao.model.Payment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PaymentRowMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();

        payment.setPayment_id(0);
        payment.setPatient_id(rs.getInt("patient_id"));
        payment.setAmount(rs.getInt("amount"));
        payment.setDate(null);

        return payment;
    }
}
