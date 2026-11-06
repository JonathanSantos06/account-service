package com.jona.bank.account.domain.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("El importe debe ser mayor que cero");
    }
}
