package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

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
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    
    /**
     * Handles POST request to register a new user account.
     * 
     * Accepts the account details in the request body,
     * validates the input, and creates a new account to be persisted
     * in the database. If there is an error, the appropriate HTTP
     * status and error message will be returned.
     * 
     * @param account The Account object containing the user's username and password. The account object is expected
     *         to have a non-blank username and a password of at least 4 characters.
     * @return A ResponseEntity containing the newly created Account object. The status is set to 200 (OK) if
     *         registration is successful, and the account details are returned in the body of the response.
     * @throws IllegalArgumentException If the username already exists, or if the username or password 
     *         do not meet the requirements.
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account createdAccount = accountService.register(account);
        return ResponseEntity.status(200).body(createdAccount);
    }
   
    /**
     * Handles POST request to Log a user into their account.
     * 
     * Accepts an Account object in the request body and checks the username and password
     * for a match in the database.  If valid, returns the matching Account.
     * Otherwise, throws InvalidLoginException, which is handled globally.
     * 
     * @param account The Account object containing the username and password.
     * @return a ResponseEntity containing the authenticated Account. The status is set to 200 (OK) if 
     *         login is successful.
     * @throws InvalidLoginException if the credentials are invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedIAccount = accountService.login(account.getUsername(), account.getPassword());
        return ResponseEntity.status(200).body(loggedIAccount);
    }

    /**
     * Handles POST request to create a new message.
     *
     * Accepts a Message object in the request body, validates the message text
     * and and the postedBy field to ensure it references an existing account in
     * the database. If valid, saves the message to the database. 
     *
     * @param message The Message object containing the text to be posted. Must include a valid
     *        postedBy account ID and valid messageText.
     * @return a ResponseEntity containing the newly created Message object, including the generated messageId.
     *         Status code is set to 200 (OK) if the message is created successfully.
     * @throws IllegalArgumentException if the message contents are invalid.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(200).body(createdMessage);
    }

}
