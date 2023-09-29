package com.sheermoon.BankDemo.repository;

import com.sheermoon.BankDemo.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends CrudRepository<Account, Long> {

    @Query("select p.id from #{#entityName} p")
    List<Long> getAllIds();

    Optional<Account> findByName(String name);
    Optional<Account> findByNumberAccount(Long numberAccount);
}
