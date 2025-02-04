package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.*;
import com.example.entity.*;
import java.util.Optional; 
import java.util.List; 
import java.util.ArrayList;
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

    @PostMapping("/login")
    public ResponseEntity<Account> verifyAccount(@RequestBody Account account) {
        //check if account is null
        if (account == null) {
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED); 

        }

        Optional<Account> verifiedAccountOptional = accountService.findByUserName(account.getUsername());   

        if (verifiedAccountOptional.isPresent()) {
            Account verifyAccount = verifiedAccountOptional.get(); 
            if (account.getPassword().equals(verifyAccount.getPassword())) {
                return new ResponseEntity<Account>(verifyAccount, HttpStatus.OK); 
            } else {
                return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED); 
            }
        } else {
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED); 

        }


    }


    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        //Should Contain the message
        Message createdMessage = messageService.createMessage(message); 
        if (createdMessage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdMessage, HttpStatus.OK);
        
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> allMessages = messageService.getAllMessages(); 
        return ResponseEntity.ok(allMessages); 
    } 

    @GetMapping("messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable("messageId") Integer messageId) {
        Message message = messageService.getMessageById(messageId);  
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return ResponseEntity.ok().body(""); 
        }   
        return ResponseEntity.ok(message); 
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable("messageId") Integer messageId) {
        //if the pass back number is 0 then none has been updated if it's greater than it has been updated. 
        int rowsUpdated = messageService.deleteMessageById(messageId);
        if (rowsUpdated > 0) {
            return ResponseEntity.ok().body(rowsUpdated); 
        } else {
            return ResponseEntity.ok().body(""); 
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<?> updateMessageById(@RequestBody Message message, @PathVariable("messageId") Integer messageId) {

        Integer rowsUpdated = messageService.updateMessage(message, messageId); 

        if (rowsUpdated == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {
            return ResponseEntity.ok(rowsUpdated);

        }
    }  
    
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccount(@PathVariable("accountId") int accountId) {
        Optional<List<Message>> allMessagesByAccountOptional = messageService.getAllMessagesByAccount(accountId); 
        //return a new empty List message if optional orElse; 
        List<Message> allMessages = allMessagesByAccountOptional.orElse(new ArrayList<Message>());
        return ResponseEntity.ok(allMessages);

    }



}
