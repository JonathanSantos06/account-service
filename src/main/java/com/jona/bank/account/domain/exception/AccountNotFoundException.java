package com.jona.bank.account.domain.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String id) {
        super("No se encontró la cuenta con identificador: " + id);
    }
}