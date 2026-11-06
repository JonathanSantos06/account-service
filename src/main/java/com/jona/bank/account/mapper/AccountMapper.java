
package com.jona.bank.account.mapper;

import com.jona.bank.account.domain.model.Account;
import com.jona.bank.account.infrastructure.adapter.out.mongodb.document.AccountDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AccountMapper {

    AccountDocument toDocument(Account account);

    Account toDomain(AccountDocument document);
}
