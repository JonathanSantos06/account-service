package com.jona.bank.account.domain.exception;

public class AccountNumberAlreadyExistsException extends RuntimeException {

    public AccountNumberAlreadyExistsException(String accountNumber) {
        super("El número de cuenta ya está registrado: " + accountNumber);
    }
}