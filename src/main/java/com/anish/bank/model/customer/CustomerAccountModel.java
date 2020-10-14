package com.anish.bank.model.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.anish.bank.bean.customer.CustomerAccount;

import org.springframework.stereotype.Service;

@Service
public class CustomerAccountModel {

    private static Map<Integer,CustomerAccount> accountCache = new HashMap<>();

    private static Map<String,Integer> tokenCache = new HashMap<>();

    public String login (CustomerAccount account) {
    
        Optional<Map.Entry<Integer, CustomerAccount>> customer = getAccountByUsername(account.getUsername());

        if (customer.isPresent() && customer.get().getValue().getPassword().equals(account.getPassword())) {
            String token = UUID.randomUUID().toString();
            tokenCache.put(token, customer.get().getValue().getId());
            return token;
        }

        return null;

    }

    public boolean createAccount (CustomerAccount account) {

        if ( account.getUsername() == null || account.getPassword() == null || account.getCustomerName() == null ) {
            return false;
        }

        Optional<Map.Entry<Integer, CustomerAccount>> customer = getAccountByUsername(account.getUsername());

        if (customer.isPresent()) {
            return false;
        }

        account.setId(new Random().nextInt());
        account.setStatus(true);

        accountCache.put(account.getId(), account);

        return true;

    }

    public CustomerAccount getCustomerAccountFromToken (String token) {

        if (token != null && tokenCache.get(token)!=null) {
            return accountCache.get(tokenCache.get(token));
        }

        return null;

    }

    private Optional<Map.Entry<Integer, CustomerAccount>> getAccountByUsername (String userName) {

        Optional<Map.Entry<Integer, CustomerAccount>> firstMatch = accountCache
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue().getUsername().equals(userName))
        .sorted(Map.Entry.comparingByKey())
        .findFirst();

        return firstMatch;

    }

}