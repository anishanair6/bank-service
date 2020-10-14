package com.anish.bank.service;

import com.anish.bank.bean.customer.CustomerAccount;
import com.anish.bank.model.customer.CustomerAccountModel;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountService {

    private CustomerAccountModel customerAccountModel = new CustomerAccountModel();
    
    public String login (CustomerAccount account) {
        return customerAccountModel.login(account);
    }

    public boolean createAccount (CustomerAccount account) {
        return customerAccountModel.createAccount(account);
    }

    public CustomerAccount getCustomerAccount (String token) {
        return customerAccountModel.getCustomerAccountFromToken(token);
    }

    @EventListener
    public void onHandleRequest(CustomerServices service) {
        switch (service.getTransactionType()) {
            case CREATE_ACCOUNT:
                service.setOutput(createAccount(service.getAccount()));
                break;
            case LOGIN:
                service.setOutput(login(service.getAccount()));
                break;
            case GET_ACCOUNT:
                service.setOutput(getCustomerAccount(service.getToken()));
                break;
            default:
                break;
        }
    }

}
