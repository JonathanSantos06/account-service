package com.jona.bank.account.domain.exception;

public class AccountInactiveException extends RuntimeException {

    public AccountInactiveException(String id) {
        super("La cuenta " + id + " no está activa y no permite operaciones");
    }
}