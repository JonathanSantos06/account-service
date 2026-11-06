package com.jona.bank.account.infrastructure.out.mongodb.repository;

import com.jona.bank.account.infrastructure.adapter.out.mongodb.document.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAccountRepository   extends MongoRepository<AccountDocument, String> {

    boolean existsByAccountNumber(String accountNumber);
}
