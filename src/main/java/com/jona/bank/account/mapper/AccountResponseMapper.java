
package com.jona.bank.account.mapper;

import com.jona.bank.account.application.dto.AccountResponse;
import com.jona.bank.account.domain.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper {

    AccountResponse toResponse(Account account);
}
