package com.anish.bank.rest;

import com.anish.bank.bean.BankAccount;
import com.anish.bank.bean.MyAccount;
import com.anish.bank.bean.TRANSACTION_TYPE;
import com.anish.bank.model.impl.CurrentAccount;
import com.anish.bank.model.impl.SavingsAccount;
import com.anish.bank.service.BankServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BankAccountController {

    private final ApplicationEventPublisher publisher;

    @Autowired
    BankAccountController (ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    private static Logger log = LoggerFactory.getLogger(BankAccountController.class);
    
    @GetMapping("/get-savings-account")
    public ResponseEntity<BankAccount> getSavingsAccount(@RequestParam("id") int id,@RequestHeader("customerToken") String token ) {

        BankServices service = new BankServices(this, id, new SavingsAccount(), token , TRANSACTION_TYPE.GET_ACCOUNT);

        publisher.publishEvent(service);

        BankAccount bankAccount = (BankAccount) service.getOutput();
        
        log.info("Get Savings Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+bankAccount);

        if (bankAccount == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok((BankAccount)bankAccount);
    }

    @PostMapping("/create-savings-account")
	public ResponseEntity<?> createSavingsAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    

        int accountId = 0;
        try {
            BankServices service = new BankServices(this, myAccount, new SavingsAccount(), token , TRANSACTION_TYPE.CREATE_ACCOUNT);

            publisher.publishEvent(service);
    
            accountId = (int) service.getOutput();

            if (accountId!=0) {
                log.info("Create Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+accountId);
                return ResponseEntity.ok(accountId);
            }
        } catch (Exception e) {
            log.error("Error", e);
        }

        log.info("Create Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+accountId);

        return ResponseEntity.badRequest().build();

    }

    @DeleteMapping("/delete-savings-account")
    public ResponseEntity<BankAccount> deleteSavingsAccount(@RequestParam("id") int id,@RequestHeader("customerToken") String token ) {    

        try {
            BankServices service = new BankServices(this, id, new SavingsAccount(), token , TRANSACTION_TYPE.DELETE_ACCOUNT);

            publisher.publishEvent(service);     

            if (! (boolean) service.getOutput()) {
                log.info("Delete Savings Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+false);
                return ResponseEntity.notFound().build();
            }
    
            log.info("Delete Savings Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+true);
            return ResponseEntity.ok().build();            
        } catch (Exception e) {
            log.error("Error", e);
        }

        return ResponseEntity.notFound().build();

    }    

    @GetMapping("/get-current-account")
    public ResponseEntity<BankAccount> getCurrentAccount(@RequestParam("id") int id,@RequestHeader("customerToken") String token ) {
        
        BankServices service = new BankServices(this, id, new CurrentAccount(), token , TRANSACTION_TYPE.GET_ACCOUNT);

        publisher.publishEvent(service);
        
        BankAccount bankAccount = (BankAccount) service.getOutput();
        
        if (bankAccount == null) {
            log.info("Get Current Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+bankAccount);
            return ResponseEntity.notFound().build();
        }

        log.info("Get Current Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+bankAccount);
        return ResponseEntity.ok((BankAccount)bankAccount);
    }

    @PostMapping("/create-current-account")
	public ResponseEntity<?> createCurrentAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    

        BankServices service = new BankServices(this, myAccount, new CurrentAccount(), token , TRANSACTION_TYPE.CREATE_ACCOUNT);

        publisher.publishEvent(service);

        int accountId = (int) service.getOutput();

        if (accountId!=0) {
            log.info("Create Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+accountId);
            return ResponseEntity.ok(accountId);
        }

        log.info("Create Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+accountId);
        return ResponseEntity.badRequest().build();

    }

    @DeleteMapping("/delete-current-account")
    public ResponseEntity<BankAccount> deleteCurrentAccount(@RequestParam("id") int id,@RequestHeader("customerToken") String token ) {    
        
        BankServices service = new BankServices(this, id, new CurrentAccount(), token , TRANSACTION_TYPE.DELETE_ACCOUNT);

        publisher.publishEvent(service);

        if (! (boolean) service.getOutput()) {
            log.info("Delete Current Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+false);            
            return ResponseEntity.notFound().build();
        }

        log.info("Delete Current Account request processed"+" [input-id] "+id+" [input-customerToken] "+token+" [output] "+false);
        return ResponseEntity.ok().build();
    }  
    
    @PostMapping("/credit-savings-account")
	public ResponseEntity<?> creditSavingsAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    

        BankServices service = new BankServices(this, myAccount, new SavingsAccount(), token , TRANSACTION_TYPE.CREDIT);

        publisher.publishEvent(service);

        if ( (boolean) service.getOutput()) {
            log.info("Credit Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+true);
            return ResponseEntity.ok().build();
        }

        log.info("Credit Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+false);
        return ResponseEntity.badRequest().build();

    }    

    @PostMapping("/debit-savings-account")
	public ResponseEntity<?> debitSavingsAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    

        BankServices service = new BankServices(this, myAccount, new SavingsAccount(), token , TRANSACTION_TYPE.DEBIT);

        publisher.publishEvent(service);

        if ( (boolean) service.getOutput()) {
            log.info("Debit Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+true);
            return ResponseEntity.ok().build();
        }

        log.info("Debit Savings Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+false);
        return ResponseEntity.badRequest().build();

    }       
    
    @PostMapping("/credit-current-account")
	public ResponseEntity<?> creditCurrentAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    

        BankServices service = new BankServices(this, myAccount, new CurrentAccount(), token , TRANSACTION_TYPE.CREDIT);

        publisher.publishEvent(service);

        if ( (boolean) service.getOutput()) {
            log.info("Credit Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+true);
            return ResponseEntity.ok().build();
        }

        log.info("Credit Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+false);
        return ResponseEntity.badRequest().build();

    }    

    @PostMapping("/debit-current-account")
	public ResponseEntity<?> debitCurrentAccount(@RequestBody MyAccount myAccount, @RequestHeader("customerToken") String token ) {    


        BankServices service = new BankServices(this, myAccount, new SavingsAccount(), token , TRANSACTION_TYPE.DEBIT);

        publisher.publishEvent(service);

        if ( (boolean) service.getOutput()) {
            log.info("Debit Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+true);
            return ResponseEntity.ok().build();
        }

        log.info("Debit Current Account request processed"+" [input-myAccount] "+myAccount.toString()+" [input-customerToken] "+token+" [output] "+false);        
        return ResponseEntity.badRequest().build();

    }        

}
