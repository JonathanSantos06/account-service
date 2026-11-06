
package com.jona.bank.account.application.dto;

import com.jona.bank.account.domain.model.AccountStatus;
import com.jona.bank.account.domain.model.AccountType;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
        String id,
        String accountNumber,
        String customerId,
        AccountType accountType,
        String currency,
        BigDecimal balance,
        AccountStatus status,
        Instant openedAt
) {
}
