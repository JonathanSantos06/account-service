
package com.jona.bank.account.infrastructure.adapter.out.mongodb;

import com.jona.bank.account.domain.model.Account;
import com.jona.bank.account.domain.model.AccountStatus;
import com.jona.bank.account.domain.port.out.AccountRepositoryPort;
import com.jona.bank.account.infrastructure.out.mongodb.repository.MongoAccountRepository;
import com.jona.bank.account.infrastructure.adapter.out.mongodb.document.AccountDocument;
import com.jona.bank.account.mapper.AccountMapper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class MongoAccountRepositoryAdapter
        implements AccountRepositoryPort {

    private final MongoAccountRepository repository;
    private final AccountMapper mapper;
    private final MongoTemplate mongoTemplate;

    public MongoAccountRepositoryAdapter(
            MongoAccountRepository repository,
            AccountMapper mapper,
            MongoTemplate mongoTemplate
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Account save(Account account) {
        var document = mapper.toDocument(account);
        var savedDocument = repository.save(document);

        return mapper.toDomain(savedDocument);
    }

    @Override
    public Optional<Account> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public Optional<Account> deposit(
            String id,
            BigDecimal amount
    ) {
        Query query = new Query(
                Criteria.where("_id").is(id)
                        .and("status").is(AccountStatus.ACTIVE)
        );

        Update update = new Update()
                .inc("balance", amount);

        AccountDocument updatedDocument = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                AccountDocument.class
        );

        return Optional.ofNullable(updatedDocument)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Account> withdraw(
            String id,
            BigDecimal amount
    ) {
        Query query = new Query(
                Criteria.where("_id").is(id)
                        .and("status").is(AccountStatus.ACTIVE)
                        .and("balance").gte(amount)
        );

        Update update = new Update()
                .inc("balance", amount.negate());

        AccountDocument updatedDocument = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                AccountDocument.class
        );

        return Optional.ofNullable(updatedDocument)
                .map(mapper::toDomain);
    }
}
