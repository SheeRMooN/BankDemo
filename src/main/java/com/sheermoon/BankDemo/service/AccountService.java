package com.sheermoon.BankDemo.service;

import com.sheermoon.BankDemo.model.Account;
import com.sheermoon.BankDemo.model.AccountDtoTransfer;
import com.sheermoon.BankDemo.model.AccountDtoUpdate;
import com.sheermoon.BankDemo.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final Long NUMBER_SECRET = 111000L;

    @Transactional
    public Account createUser(Account account) {
        try {
            int number = Integer.parseInt(account.getPinCode());
            if (number > 9999 || number < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пинкод должен состоять из четырех цыфр");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Используйте цыфры для пинкода");
        }
        System.out.println(account);
        accountRepo.save(account);
        account.setNumberAccount(account.getId() + NUMBER_SECRET);
        return account;
    }

    @Transactional
    public Account refreshBalance(AccountDtoUpdate accountDtoUpdate) {
        Account account = checkAccount(accountDtoUpdate.getName(), accountDtoUpdate.getPinCode());
        Long balance = account.getBalance();
        if (balance + accountDtoUpdate.getRefreshBalance() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вы не можите иметь отрицательный баланс");
        }
        account.setBalance(balance + accountDtoUpdate.getRefreshBalance());
        return account;
    }

    @Transactional
    public Account transfer(AccountDtoTransfer accountDtoTransfer) {
        Account account = checkAccount(accountDtoTransfer.getName(), accountDtoTransfer.getPinCode());

        Optional<Account> secondAccount = accountRepo.findByNumberAccount(accountDtoTransfer.getAnotherAccount());
        secondAccount.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя перевести средства нету такого аккаунта");
        });
        if (accountDtoTransfer.getRefreshBalance() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя снимать средства с чужого аккаунта, авторизируйтесь другим аккаунтом");
        }
        if (account.getBalance() - accountDtoTransfer.getRefreshBalance() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вы не можите иметь отрицательный баланс");
        }
        long newBalance = account.getBalance() - accountDtoTransfer.getRefreshBalance();

        account.setBalance(newBalance);
        return account;
    }

    public Optional<List<Account>> getAllInfo() {

        Iterable<Account> iterable = accountRepo.findAll();
        if (!iterable.iterator().hasNext()) {
            return Optional.of(new ArrayList<Account>());
        }

        return Optional.of(StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList())
        );
    }

    private Account checkAccount(String name, String pinCode) {
        Optional<Account> optional = accountRepo.findByName(name);
        optional.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильное Имя");
        });
        Account accBase = optional.get();
        if (!accBase.getPinCode().equals(pinCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильное Пинкод");
        }
        return optional.get();
    }

}
