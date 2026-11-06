package com.jona.bank.account.domain.port.out;

import com.jona.bank.account.domain.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);

    Optional<Account> findById(String id);

    List<Account> findAll();

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> deposit(String id, BigDecimal amount);

    Optional<Account> withdraw(String id, BigDecimal amount);

}
