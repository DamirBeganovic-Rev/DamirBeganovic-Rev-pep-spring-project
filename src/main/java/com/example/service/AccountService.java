package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public Account register(Account newAccount) throws IllegalArgumentException{
        // Check if username already exists
        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already exists. Try a different username");
        }
        // Check that username is not blank
        if (newAccount.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        // Check that password is at least 4 characters long
        if (newAccount.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password needs to be at least 4 characters long");
        }
        // Save the new account to the database if it meets all of the requirements
        return accountRepository.save(newAccount);
    }
   
}
