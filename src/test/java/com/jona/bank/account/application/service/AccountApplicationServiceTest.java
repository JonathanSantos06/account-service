
package com.jona.bank.account.application.service;

import com.jona.bank.account.application.dto.CreateAccountRequest;
import com.jona.bank.account.domain.exception.AccountNotFoundException;
import com.jona.bank.account.domain.model.Account;
import com.jona.bank.account.domain.model.AccountStatus;
import com.jona.bank.account.domain.model.AccountType;
import com.jona.bank.account.domain.port.out.AccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    private AccountApplicationService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountApplicationService(accountRepository);
    }

    @Test
    void shouldCreateAccountWithZeroBalance() {
        CreateAccountRequest request = mock(CreateAccountRequest.class);

        when(request.customerId()).thenReturn("customer-123");
        when(request.accountType()).thenReturn(AccountType.SAVINGS);
        when(request.currency()).thenReturn("MXN");
        when(accountRepository.existsByAccountNumber(anyString()))
                .thenReturn(false);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("customer-123", result.getCustomerId());
        assertEquals(AccountType.SAVINGS, result.getAccountType());
        assertEquals("MXN", result.getCurrency());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(AccountStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getOpenedAt());
        assertNotNull(result.getAccountNumber());
        assertEquals(16, result.getAccountNumber().length());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldReturnAllAccounts() {
        Account first = new Account();
        Account second = new Account();

        when(accountRepository.findAll()).thenReturn(List.of(first, second));

        List<Account> result = accountService.findAll();

        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    void shouldReturnAccountWhenIdExists() {
        Account account = new Account();

        when(accountRepository.findById("account-123"))
                .thenReturn(Optional.of(account));

        Account result = accountService.findById("account-123");

        assertSame(account, result);
        verify(accountRepository).findById("account-123");
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {
        String id = "account-missing";

        when(accountRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.findById(id)
        );

        verify(accountRepository).findById(id);
    }

    @Test
    void shouldRetryWhenGeneratedAccountNumberAlreadyExists() {
        CreateAccountRequest request = mock(CreateAccountRequest.class);

        when(request.customerId()).thenReturn("customer-123");
        when(request.accountType()).thenReturn(AccountType.SAVINGS);
        when(request.currency()).thenReturn("MXN");

        when(accountRepository.existsByAccountNumber(anyString()))
                .thenReturn(true, false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.createAccount(request);

        assertNotNull(result.getAccountNumber());
        assertEquals(16, result.getAccountNumber().length());

        verify(accountRepository, times(2))
                .existsByAccountNumber(anyString());
        verify(accountRepository).save(any(Account.class));
    }
}
