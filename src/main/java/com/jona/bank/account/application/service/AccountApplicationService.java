
package com.jona.bank.account.application.service;

import com.jona.bank.account.application.dto.CreateAccountRequest;
import com.jona.bank.account.domain.exception.AccountInactiveException;
import com.jona.bank.account.domain.exception.AccountNotFoundException;
import com.jona.bank.account.domain.exception.InsufficientBalanceException;
import com.jona.bank.account.domain.exception.InvalidAmountException;
import com.jona.bank.account.domain.model.Account;
import com.jona.bank.account.domain.model.AccountStatus;
import com.jona.bank.account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

@Service
public class AccountApplicationService {

    private static final int MAX_ACCOUNT_NUMBER_ATTEMPTS = 10;

    private final AccountRepositoryPort accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AccountApplicationService(
            AccountRepositoryPort accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setCustomerId(request.customerId());
        account.setAccountType(request.accountType());
        account.setCurrency(request.currency());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setOpenedAt(Instant.now());

        return accountRepository.save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private String generateAccountNumber() {
        for (int attempt = 0;
             attempt < MAX_ACCOUNT_NUMBER_ATTEMPTS;
             attempt++) {

            StringBuilder number = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                number.append(secureRandom.nextInt(10));
            }

            String candidate = number.toString();

            if (!accountRepository.existsByAccountNumber(candidate)) {
                return candidate;
            }
        }

        throw new IllegalStateException(
                "No fue posible generar un número de cuenta disponible"
        );
    }

    public Account deposit(String id, BigDecimal amount) {
        validateAmount(amount);
        validateAccountStatus(findById(id));

        return accountRepository.deposit(id, amount)
                .orElseThrow(() -> resolveOperationFailure(id, amount, false));
    }

    public Account withdraw(String id, BigDecimal amount) {
        validateAmount(amount);
        validateAccountStatus(findById(id));

        return accountRepository.withdraw(id, amount)
                .orElseThrow(() -> resolveOperationFailure(id, amount, true));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException(account.getId());
        }
    }


    private RuntimeException resolveOperationFailure(
            String id,
            BigDecimal amount,
            boolean withdrawal
    ) {
        Account currentAccount = findById(id);

        validateAccountStatus(currentAccount);

        if (withdrawal &&
                currentAccount.getBalance().compareTo(amount) < 0) {
            return new InsufficientBalanceException();
        }

        return new IllegalStateException(
                "No fue posible completar la operación bancaria"
        );
    }


}
