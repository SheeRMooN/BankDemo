package com.sheermoon.BankDemo.model;

import lombok.Data;

@Data
public class AccountDtoTransfer {

    private String name;
    private String pinCode;
    private Long refreshBalance;
    private Long anotherAccount;
}
