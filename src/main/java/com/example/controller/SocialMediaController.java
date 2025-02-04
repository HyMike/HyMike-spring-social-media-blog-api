package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.*;
import com.example.entity.*;
import java.util.Optional; 
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService; 
    private MessageService messageService; 

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {

        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
 
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        Optional<Account> existingAccount = accountService.findByUserName(account.getUsername());
        if (existingAccount.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } 
        
        Account createdAccount = accountService.createAccount(account); 
        return new ResponseEntity<>(createdAccount, HttpStatus.OK); 
        


    }


}
