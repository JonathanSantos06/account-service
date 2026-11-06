
package com.jona.bank.account.infrastructure.adapter.out.mongodb.document;

import com.jona.bank.account.domain.model.AccountStatus;
import com.jona.bank.account.domain.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class AccountDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;

    @Indexed
    private String customerId;

    private AccountType accountType;

    private String currency;

    private BigDecimal balance;

    private AccountStatus status;

    private Instant openedAt;
}
