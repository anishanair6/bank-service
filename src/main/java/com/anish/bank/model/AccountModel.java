package com.anish.bank.model;

import com.anish.bank.bean.Account;

public interface AccountModel {
    
    int createAccount (Account account);

    boolean deleteAccount (int id, int customerId);

    Account retrieveAccount (int id, int customerId);

    boolean debit (float amount, int id, int customerId);

    boolean credit (float amount, int id, int customerId);

}
