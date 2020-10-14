package com.anish.bank.model.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.anish.bank.bean.Account;
import com.anish.bank.bean.BankAccount;
import com.anish.bank.model.AccountModel;

@Service
public class CurrentAccount implements AccountModel {

    private static Map<Integer, BankAccount> accountCache = new HashMap<>();

    @Override
    public boolean deleteAccount(int id, int customerId) {

        if (accountCache.get(id) == null) {
            return false;
        }

        if (accountCache.get(id).getCustomerId()!=customerId) {
            return false;
        }        

        accountCache.remove(id);
        return true;
        
    }

    @Override
    public Account retrieveAccount(int id, int customerId) {

        if (accountCache.get(id).getCustomerId()!=customerId) {
            return null;
        }

        return accountCache.get(id);
    }

    @Override
    public int createAccount(Account account) {
        BankAccount bankAccount = (BankAccount) account;
        if (bankAccount.getId()!=0 && accountCache.get(bankAccount.getId()) == null) {
            accountCache.put(bankAccount.getId(),bankAccount);
            return bankAccount.getId();
        }
        return 0;
    }

    @Override
    public boolean debit(float amount, int id , int customerId) {

        BankAccount account = accountCache.get(id);

        if (account == null) {
            return false;
        }

        if (account.getCustomerId()!=customerId) {
            return false;
        }
        
        if (account.getBalance() < amount ) {
            return false;
        }

        account.setBalance(account.getBalance()-amount);

        return true;
    }

    @Override
    public boolean credit(float amount, int id, int customerId) {

        BankAccount account = accountCache.get(id);

        if (account == null) {
            return false;
        }

        if (account.getCustomerId()!=customerId) {
            return false;
        }        

        account.setBalance(account.getBalance()+amount);

        return true;

    }

}
