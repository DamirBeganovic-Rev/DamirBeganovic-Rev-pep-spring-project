package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService){
        this.accountService = accountService;
    }

    
    /**
     * Registers a new user account
     * 
     * It accepts the account details in the request body,
     * validates the input, and creates a new account to be persisted
     * in the database. If there is an error, the appropriate HTTP
     * status and error message will be returned.
     * 
     * @param account The Account object containing the user's username and password. The account object is expected
     *         to have a non-blank username and a password of at least 4 characters.
     * @return A ResponseEntity containing the created Account object. The status is set to 200 OK if
     *         registration is successful, and the account details are returned in the body of the response.
     * @throws IllegalArgumentException If the username already exists, or if the username or password 
     *         do not meet the requirements
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account createdAccount = accountService.register(account);
        return ResponseEntity.status(200).body(createdAccount); // Successful registration
    }
   

}
