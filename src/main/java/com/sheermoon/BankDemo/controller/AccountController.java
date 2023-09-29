package com.sheermoon.BankDemo.controller;

import com.sheermoon.BankDemo.model.Account;
import com.sheermoon.BankDemo.model.AccountDtoUpdate;
import com.sheermoon.BankDemo.model.AccountDtoTransfer;
import com.sheermoon.BankDemo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> creatUser(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createUser(account));
    }

    @PostMapping("/update")
    public ResponseEntity<Account> refreshBalance(@RequestBody AccountDtoUpdate accountDtoUpdate) {
        return ResponseEntity.ok(accountService.refreshBalance(accountDtoUpdate));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Account> transferMoney(@RequestBody AccountDtoTransfer accountDtoTransfer) {
        return ResponseEntity.ok(accountService.transfer(accountDtoTransfer));
    }

    @GetMapping("/getInfo")
    public ResponseEntity<Optional<List<Account>>> infoAll() {
        return new ResponseEntity<>(accountService.getAllInfo(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/createAccounts")
    public ResponseEntity<List<Account>> getJson() {
        List<Account> accounts = List.of(
                new Account(null, "Vova", "1111", null, 1000L),
                new Account(null, "Ivan", "1111", null, 2000L),
                new Account(null, "Ira", "1111", null, 3000L),
                new Account(null, "Sveta", "1111", null, 4000L),
                new Account(null, "Andrei", "1111", null, 5000L),
                new Account(null, "Anna", "1111", null, 6000L),
                new Account(null, "Victor", "1111", null, 7000L)
        );
        accounts.forEach(accountService::createUser);
        return ResponseEntity.ok(accounts);
    }
}
