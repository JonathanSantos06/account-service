
package com.jona.bank.account.infrastructure.adapter.in.web;

import com.jona.bank.account.application.dto.AccountResponse;
import com.jona.bank.account.application.dto.CreateAccountRequest;
import com.jona.bank.account.application.dto.MoneyOperationRequest;
import com.jona.bank.account.application.service.AccountApplicationService;
import com.jona.bank.account.mapper.AccountResponseMapper;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountApplicationService accountService;
    private final AccountResponseMapper responseMapper;

    public AccountController(
            AccountApplicationService accountService,
            AccountResponseMapper responseMapper
    ) {
        this.accountService = accountService;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        return responseMapper.toResponse(
                accountService.createAccount(request)
        );
    }

    @GetMapping
    public List<AccountResponse> findAll() {
        return accountService.findAll()
                .stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable String id) {
        return responseMapper.toResponse(
                accountService.findById(id)
        );
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(
            @PathVariable String id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        return responseMapper.toResponse(
                accountService.deposit(id, request.amount())
        );
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(
            @PathVariable String id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        return responseMapper.toResponse(
                accountService.withdraw(id, request.amount())
        );
    }
}
