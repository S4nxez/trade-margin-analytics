package com.hospitalcrud.domain.errors;

public class DuplicatedUserError extends RuntimeException {
    public DuplicatedUserError(String message) {
        super(message);
    }
}