package com.anish.bank.bean;

public class BankAccount extends Account {

    private int customerId;

    private float balance;

    public int getCustomerId () {
        return customerId;
    }

    public void setCustomerId (int customerId) {
        this.customerId =  customerId;
    }

    public float getBalance () {
        return balance;
    }

    public void setBalance (float balance) {
        this.balance = balance;
    }    

}
