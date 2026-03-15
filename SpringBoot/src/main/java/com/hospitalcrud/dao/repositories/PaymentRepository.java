package com.hospitalcrud.dao.repositories;

import com.hospitalcrud.dao.model.Payment;

import java.util.List;

public interface PaymentRepository {
    List<Payment> getAll();
}
