package com.anish.bank;

import com.anish.bank.bean.MyAccount;
import com.anish.bank.bean.customer.CustomerAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class BankApplicationTests {

    @Autowired
    private MockMvc mockMvc;
  
    @Autowired
    private ObjectMapper objectMapper;

    private static Logger log = LoggerFactory.getLogger(BankApplicationTests.class);

    @Test
    @Order(1)
    void customerInvalidLogin() throws Exception {
      
        CustomerAccount custmerAccount = new CustomerAccount();
        custmerAccount.setPassword("password");
        custmerAccount.setUsername("username1");
  
        mockMvc.perform(post("/login/")
              .contentType("application/json")
              .content(objectMapper.writeValueAsString(custmerAccount)))
              .andExpect(status().isForbidden());
  
    }

    @Test
    @Order(2)
    void customerOnboard() throws Exception {
      
        CustomerAccount custmerAccount = new CustomerAccount();
        custmerAccount.setPassword("password");
        custmerAccount.setUsername("username");
        custmerAccount.setCustomerName("custmerName");
  
        mockMvc.perform(post("/create-account/")
              .contentType("application/json")
              .content(objectMapper.writeValueAsString(custmerAccount)))
              .andExpect(status().isOk());

    }    

    String customerValidLogin() throws Exception {
      
        CustomerAccount custmerAccount = new CustomerAccount();
        custmerAccount.setPassword("password");
        custmerAccount.setUsername("username");
  
        String token = mockMvc.perform(post("/login/")
              .contentType("application/json")
              .content(objectMapper.writeValueAsString(custmerAccount)))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Customer Token : "+token);

        return token;
    }

    int createSavingsAccount() throws Exception {
      
        MyAccount bankAccount = new MyAccount();
        bankAccount.setAmount(73788);
        bankAccount.setMapCode("AX7377#Mj28");
  
        String response = mockMvc.perform(post("/create-savings-account/")
              .contentType("application/json").header("customerToken", customerValidLogin())
              .content(objectMapper.writeValueAsString(bankAccount)))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  
        int accountId = Integer.parseInt(response);

        log.debug("accountId : "+accountId);

        assertNotEquals(0, accountId);

        return accountId;

    }

    @Test
    @Order(3)
    String getSavingsAccount() throws Exception {
      
        int accountId = createSavingsAccount();
  
        String response = mockMvc.perform(get("/get-savings-account?id="+accountId)
              .contentType("application/json").header("customerToken", customerValidLogin()))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Get Savings account : "+response);

        return response;

    }  

    @Test
    @Order(4)
    void debitSavingsAccount() throws Exception {
      
        String account = getSavingsAccount();
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        int accountId = Integer.parseInt(String.valueOf(jsonParser.parseMap(account).get("id")));
        MyAccount bankAccount = new MyAccount();
        bankAccount.setAmount(88);
        bankAccount.setId(accountId);
  
        String response = mockMvc.perform(post("/debit-savings-account/")
              .contentType("application/json").header("customerToken", customerValidLogin())
              .content(objectMapper.writeValueAsString(bankAccount)))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Debit Savings account : "+response);

        String responseAfterDebit = mockMvc.perform(get("/get-savings-account?id="+accountId)
              .contentType("application/json").header("customerToken", customerValidLogin()))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Savings account after debit : "+responseAfterDebit);

    }

    @Test
    @Order(5)
    void creditSavingsAccount() throws Exception {
      
        String account = getSavingsAccount();
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        int accountId = Integer.parseInt(String.valueOf(jsonParser.parseMap(account).get("id")));
        MyAccount bankAccount = new MyAccount();
        bankAccount.setAmount(1234);
        bankAccount.setId(accountId);
  
        String response = mockMvc.perform(post("/credit-savings-account/")
              .contentType("application/json").header("customerToken", customerValidLogin())
              .content(objectMapper.writeValueAsString(bankAccount)))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Credit Savings account : "+response);

        String responseAfterDebit = mockMvc.perform(get("/get-savings-account?id="+accountId)
              .contentType("application/json").header("customerToken", customerValidLogin()))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        System.out.println("Savings account after credit : "+responseAfterDebit);

    }   
    
    @Test
    @Order(6)
    void deleteSavingsAccount() throws Exception {
      
        String account = getSavingsAccount();
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        int accountId = Integer.parseInt(String.valueOf(jsonParser.parseMap(account).get("id")));
        String customerToken = customerValidLogin();
  
        String response = mockMvc.perform(delete("/delete-savings-account?id="+accountId)
              .contentType("application/json").header("customerToken", customerToken))
              .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.debug("Delete Savings account : "+response);

    }      

}
