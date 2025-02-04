package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository; 
    }

    public Account createAccount(Account account){
        return accountRepository.save(account); 
    }

    public Optional<Account> findByUserName(String username) {
        return accountRepository.findByUsername(username);
    }

}
