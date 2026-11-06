package com.jona.bank.account.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("La cuenta no tiene saldo suficiente para realizar el retiro");
    }
}