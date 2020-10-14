package com.anish.bank.service;

import com.anish.bank.bean.MyAccount;
import com.anish.bank.bean.TRANSACTION_TYPE;
import com.anish.bank.model.AccountModel;
import org.springframework.context.ApplicationEvent;

public class BankServices extends ApplicationEvent {

    /**
     *
     */
    private static final long serialVersionUID = -7683260678954453186L;

    private MyAccount account;

    private int accountId;

    private AccountModel accountModel;

    private String token;

    private Object output;

    private TRANSACTION_TYPE transactionType;

    public BankServices(Object source, int accountId, AccountModel accountModel, String token,
            TRANSACTION_TYPE transactionType) {
        super(source);
        this.setAccountId(accountId);
        this.setAccountModel(accountModel);
        this.setToken(token);
        this.setTransactionType(transactionType); 
    }

    public TRANSACTION_TYPE getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TRANSACTION_TYPE transactionType) {
        this.transactionType = transactionType;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public MyAccount getAccount() {
        return account;
    }

    public void setAccount(MyAccount account) {
        this.account = account;
    }

    public BankServices(Object source, MyAccount account, AccountModel accountModel, String token, TRANSACTION_TYPE transactionType) {
        super(source);
        this.setAccount(account);
        this.setAccountModel(accountModel);
        this.setToken(token);
        this.setTransactionType(transactionType);
    }
    
}
