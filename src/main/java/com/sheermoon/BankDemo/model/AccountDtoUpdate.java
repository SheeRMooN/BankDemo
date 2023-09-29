package com.sheermoon.BankDemo.model;

import lombok.Data;

@Data
public class AccountDtoUpdate {

    private String name;

    private String pinCode;

    private int refreshBalance;
}
