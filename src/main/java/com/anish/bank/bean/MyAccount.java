package com.anish.bank.bean;

public class MyAccount extends Account {
    
    private String mapCode;

    private float amount;

    public String getMapCode () {
        return mapCode;
    }

    public float getAmount () {
        return amount;
    }

    public void setMapCode (String mapCode) {
        this.mapCode = mapCode;
    }

    public void setAmount (float amount) {
        this.amount = amount;
    }

}
