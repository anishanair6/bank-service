package com.anish.bank.rest;

import com.anish.bank.bean.TRANSACTION_TYPE;
import com.anish.bank.bean.customer.CustomerAccount;
import com.anish.bank.service.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAccountController {

    private final ApplicationEventPublisher publisher;

    @Autowired
    CustomerAccountController (ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }    
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomerAccount myAccount) {    

        CustomerServices service = new CustomerServices(this, myAccount, TRANSACTION_TYPE.LOGIN);

        publisher.publishEvent(service);

        String token = (String) service.getOutput();

        if (token == null || "".equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(token);
    }

    @PostMapping("/create-account")
	public ResponseEntity<?> createSavingsAccount(@RequestBody CustomerAccount myAccount) {    

        CustomerServices service = new CustomerServices(this, myAccount, TRANSACTION_TYPE.CREATE_ACCOUNT);

        publisher.publishEvent(service);

        if ( (boolean) service.getOutput()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();

    }    

}
