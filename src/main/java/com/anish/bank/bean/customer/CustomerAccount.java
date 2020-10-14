package com.anish.bank.bean.customer;

import com.anish.bank.bean.Account;

public class CustomerAccount extends Account {

    private String customerName;

    private String password;

    private String username;

    public void setCustomerName (String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName () {
        return this.customerName;
    }


    public void setPassword (String password) {
        this.password = password;
    }

    public String getPassword () {
        return this.password;
    }
    

    public void setUsername (String username) {
        this.username = username;
    }

    public String getUsername () {
        return this.username;
    }    

}
