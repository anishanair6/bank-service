package com.anish.bank.service;

import java.util.Random;
import com.anish.bank.bean.Account;
import com.anish.bank.bean.BankAccount;
import com.anish.bank.bean.customer.CustomerAccount;
import com.anish.bank.bean.MyAccount;
import com.anish.bank.bean.TRANSACTION_TYPE;
import com.anish.bank.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BankAccountService {

    private AccountModel account;

    private final ApplicationEventPublisher publisher;

    @Autowired
    BankAccountService (ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    BankAccount getBankAccount(int id,String token ) {
        
        CustomerAccount customerAccount = getCustomerInfo(token);

        System.out.println("customer account : "+customerAccount);

        if (customerAccount == null) {
            return null;
        }

        Account bankAccount = account.retrieveAccount(id, customerAccount.getId());

        return (BankAccount) bankAccount;
    }

	int createBankAccount(MyAccount myAccount,String token) {    

        CustomerAccount customerAccount = getCustomerInfo(token);

        if (customerAccount == null) {
            return 0;
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(myAccount.getAmount());
        bankAccount.setCustomerId(customerAccount.getId());
        bankAccount.setId(Math.abs(new Random().nextInt()));
        bankAccount.setStatus(true);

        return account.createAccount(bankAccount);       

    }    

    boolean deleteBankAccount(int id,String token ) {    
        
        CustomerAccount customerAccount = getCustomerInfo(token);

        if (customerAccount == null) {
            return false;
        }

        return account.deleteAccount(id, customerAccount.getId());

    }       

    boolean credit (MyAccount myAccount, String token) {

        CustomerAccount customerAccount = getCustomerInfo(token);

        if (customerAccount == null) {
            return false;
        }

        return account.credit(myAccount.getAmount(),myAccount.getId(),customerAccount.getId());

    }

    boolean debit (MyAccount myAccount, String token) {

        CustomerAccount customerAccount = getCustomerInfo(token);

        if (customerAccount == null) {
            return false;
        }

        return account.debit(myAccount.getAmount(),myAccount.getId(),customerAccount.getId());

    }    


    CustomerAccount getCustomerInfo (String token) {

        if (token == null || "".equals(token)) {
            return null;
        }

        CustomerServices service = new CustomerServices(this, token, TRANSACTION_TYPE.GET_ACCOUNT);

        publisher.publishEvent(service);     

        return (CustomerAccount) service.getOutput();

    }

    @EventListener
    public void onHandleRequest(BankServices service) {
        this.account =  service.getAccountModel();
        switch (service.getTransactionType()) {
            case CREATE_ACCOUNT:
                service.setOutput(createBankAccount(service.getAccount(), service.getToken()));
                break;
            case GET_ACCOUNT:
                service.setOutput(getBankAccount(service.getAccountId(), service.getToken()));
                break;  
            case CREDIT:
                service.setOutput(credit(service.getAccount(), service.getToken()));
                break;
            case DEBIT:
                service.setOutput(debit(service.getAccount(), service.getToken()));
                break;
            case DELETE_ACCOUNT:
                service.setOutput(deleteBankAccount(service.getAccountId(), service.getToken()));
                break;
            default:
                break;
        }
    }
}
