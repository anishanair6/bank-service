package com.anish.bank.service;

import com.anish.bank.bean.TRANSACTION_TYPE;
import com.anish.bank.bean.customer.CustomerAccount;
import org.springframework.context.ApplicationEvent;

public class CustomerServices extends ApplicationEvent {

    /**
     *
     */
    private static final long serialVersionUID = -7683260678954453186L;

    private CustomerAccount account;

    private String token;

    private Object output;

    private TRANSACTION_TYPE transactionType;

    public TRANSACTION_TYPE getTransactionType() {
        return transactionType;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

    public CustomerAccount getAccount() {
        return account;
    }

    public void setAccount(CustomerAccount account) {
        this.account = account;
    }

    public CustomerServices(Object source, CustomerAccount account, TRANSACTION_TYPE transactionType) {
        super(source);
        this.setAccount(account);
        this.setTransactionType(transactionType);
    }

    public CustomerServices(Object source, String token, TRANSACTION_TYPE transactionType) {
        super(source);
        this.setToken(token);
        this.setTransactionType(transactionType);
    }    
    
}
