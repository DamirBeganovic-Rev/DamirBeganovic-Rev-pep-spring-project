package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidLoginException;
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
     * @return A ResponseEntity containing the newly created Account object. The HTTP Status is set to 200 (OK) if
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
     * @return a ResponseEntity containing the authenticated Account. The HTTP Status is set to 200 (OK) if 
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
     *         HTTP Status code is set to 200 (OK) if the message is created successfully.
     * @throws IllegalArgumentException if the message contents are invalid.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(200).body(createdMessage);
    }

    /**
     * Handles GET request to retrieve all messages.
     * 
     * This method fetches all messages from the database. If there are no messages
     * in the database, it will return an empty list. In both cases, the HTTP Status code is 200 (OK).
     * 
     * @return A ResponseEntity containing a list of all messages and a HTTP Status code 200 (OK)
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(allMessages);
    }

    /**
     * Handles GET request to retrieve a message with the provided message ID.
     * 
     * This method fetches a message from the database using the provided message ID.
     * If the message exists, it will be returned in the response body. If no message
     * is found, the response body will be empty, but the HTTP status code will still be 200 (OK).
     *  
     * @param messageId the unique ID for the message to be retrieved
     * @return A ResponseEntity containing the retrieved Message if it is found
     *         or an empty reponse body if it is not found.  The HTTP Status code
     *         is set to 200 (OK) in both cases.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(200).body(message);
    }

    /**
     * Handles DELETE request to remove a message by its ID.
     *
     * This method attempts to delete a message with the specified message ID.
     * If the message existed and was deleted, the response body will contain the number 1.
     * If the message does not exist, the response body will be empty.
     * In both cases, the HTTP Status is 200 (OK).
     *
     * @param messageId the unique ID of the message to be deleted
     * @return a ResponseEntity with status 200 (OK). The response body contains 1 if a message was deleted,
     *         or is empty if no message was found.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        int numberOfRowsAffected = messageService.deleteMessageById(messageId);
        // If no message was found to delete, return 200 (OK) with an empty response body
        if (numberOfRowsAffected == 0){
          return ResponseEntity.status(200).build();  
        }
        // Message was found and deleted successfully, return 200 (OK) with the response body
        return ResponseEntity.status(200).body(numberOfRowsAffected);
    }

    /**
     * Handles PATCH request to update the text of an existing message.
     * 
     * This method takes the `messageId` from the URL path and the updated message content 
     * from the request body as a `Message` object, and calls the service to perform the update. 
     * If the update is succesful, the response body will contain the number of rows affected (1)
     * and the HTTP Status will be 200 (OK)
     * If the message does not exist or the new message text is invalid (blank or exceeds 255 characters),
     * an IllegalArgumentException will be thrown (handled by global exception handler) 
     * and the the HTTP Status will be 400 (Client Error).
     * 
     * @param messageId the ID of the message to be updated.
     * @param updatedMessage the message object containing the new text to be updated
     * @return a ResponseEntity containing the number of rows affected (1 if the update is successful)
     * @throws IllegalArgumentException if the message does not exist or the message text is invalid (blank or exceeds 255 characters)
     */
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message updatedMessage){
        // Call the service method to update the message using the provided messageId and the new message text
        int numberOfRowsAffected = messageService.updateMessage(messageId, updatedMessage.getMessageText());
        // Return the number of rows affected in the response body with a 200 (OK) status code
        return ResponseEntity.status(200).body(numberOfRowsAffected);
    }

    /**
     * Handles GET request to retrieve all messages posted by a specific user, identified by their account ID.
     *
     * This method retrieves a list of messages associated with the provided accountId. 
     * If there are no messages for the specified account, an empty list will be returned.
     * In both cases, the HTTP Status will be 200 (OK)
     *
     * @param accountId The unique identifier for the account whose messages are to be retrieved.
     *        This ID corresponds to an existing account in the system.
     * @return A ResponseEntity containing the HTTP status code (200 OK) and a list of Message objects.
     *         The list will be empty if no messages exist for the given account.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMesssagesFromUser(@PathVariable int accountId){
        List<Message> allMessagesFromUser = messageService.getAllMessagesFromUser(accountId);
        return ResponseEntity.status(200).body(allMessagesFromUser);
    }

}
