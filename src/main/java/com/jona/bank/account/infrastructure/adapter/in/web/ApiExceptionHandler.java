
package com.jona.bank.account.infrastructure.adapter.in.web;

import com.jona.bank.account.domain.exception.AccountInactiveException;
import com.jona.bank.account.domain.exception.AccountNotFoundException;
import com.jona.bank.account.domain.exception.AccountNumberAlreadyExistsException;
import com.jona.bank.account.domain.exception.InsufficientBalanceException;
import com.jona.bank.account.domain.exception.InvalidAmountException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(
            AccountNotFoundException exception
    ) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler({
            AccountNumberAlreadyExistsException.class,
            DuplicateKeyException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateKey(
            RuntimeException exception
    ) {
        return Map.of(
                "error",
                "El número de cuenta ya está registrado"
        );
    }

    @ExceptionHandler(AccountInactiveException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleInactiveAccount(
            AccountInactiveException exception
    ) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleInsufficientBalance(
            InsufficientBalanceException exception
    ) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidAmount(
            InvalidAmountException exception
    ) {
        return Map.of("error", exception.getMessage());
    }
}
