package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidLoginException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    /**
     * Registers a new user account.
     *
     * Validates the provided Account object and persists it in the database
     * if it meets all requirements (username uniqueness, non-blank username,
     * and password of at least 4 characters).
     *
     * @param newAccount the Account object containing the user's registration details.
     * @return the persisted Account with a generated account ID.
     * @throws IllegalArgumentException if the username is taken, blank,
     *         or the password is too short.
     */
    public Account register(Account newAccount) throws IllegalArgumentException{
        // Check if username already exists in the database
        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already exists. Try a different username");
        }
        // Ensure that the username is not blank
        if (newAccount.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        // Ensure that the password meets the minimum length requirement
        if (newAccount.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password needs to be at least 4 characters long");
        }
        // Save the new account and return it if it meets all of the requirements
        return accountRepository.save(newAccount);
    }

    /**
     * Logs a user into their account by validating their credentials.
     *
     * Looks up the account in the database using the provided username and password.
     * If a matching account is found, it is returned.
     *
     * @param username the username entered by the user.
     * @param password the password entered by the user.
     * @return the Account object from the database that matches the credentials.
     * @throws InvalidLoginException if the credentials are invalid or no match is found.
     */
    public Account login(String username, String password) throws InvalidLoginException {
        // Attempt to find an account matching the provided credentials
        return accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new InvalidLoginException("Invalid username or password."));
    }
   
}
