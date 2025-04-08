package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new message.
     * 
     * Validates the provided Message object and persists it in the database
     * if it meets all requirements (postedBy Id exists in the database, text is non-blank
     * and text is not longer than 255 characters).
     * @param newMessage the Message object containing the postedBy ID (the Account that is posting),
     *        the message text, and the time it is posted.
     * @return the persisted Message with a generated message ID.
     * @throws IllegalArgumentException if the postedBy ID (Account) of the message does not exist
     *         in the database or if the message text is blank or too long
     */
    public Message createMessage(Message newMessage) throws IllegalArgumentException{
        // Ensure the account associated with postedBy exists
        if (!accountRepository.existsById(newMessage.getPostedBy())){
            throw new IllegalArgumentException("The account does not exist.");
        }
        // Validate message text. Must not be blank or exceed 255 characters
        if (newMessage.getMessageText() == null || 
            newMessage.getMessageText().isBlank() || 
            newMessage.getMessageText().isEmpty() ||
            newMessage.getMessageText().length() > 255){
            throw new IllegalArgumentException("Message cannot be blank or over 255 characters.");
        }

        return messageRepository.save(newMessage);
    }

    /**
     * Retrieves all messsages stored in the database
     * 
     * @return A list of all Message objects
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its unique message ID
     * 
     * This method attempts to retrieve the message from the database by the
     * provided message ID.  If the message exists, it is returned.  If it doesn't
     * exist, then null is returned.
     * @param id the message ID of the Message to retrieve
     * @return the Message object if it is found, or null if no message exists
     *         in the database with the provided message ID
     */
    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a message by its unique message ID
     * 
     * This method attempts to retrieve the message from the database and then delete it
     * if it is found. It returns 1 to indicate that a row was deleted. If the message does
     * not exist, it returns 0 to indicate that no rows were deleted. This allows the 
     * controller to determine the appropriate response.
     * 
     * @param id the message Id of the Message to delete
     * @return 1 if the message existed and was deleted; 0 if the message was not found
     */
    public int deleteMessageById(int id) {
        if (messageRepository.findById(id).isPresent()){
            messageRepository.deleteById(id);
            return 1; // 1 row was deleted
        }
        return 0; // No rows were deleted, the message was not found
    }

    /**
     * Updates the text of an existing message in the database.
     * 
     * @param id The unique ID of the message to be updated
     * @param messageText The new text string of the message
     * @return The number of rows affected (should be 1 if the update is successful)
     * @throws IllegalArgumentException if the message does not exist or if the message text
     *         is blank or greater than 255 characters
     */
    public int updateMessage(int id, String messageText) throws IllegalArgumentException{
        // Ensure the Message associated with id exists
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isEmpty()){
            throw new IllegalArgumentException("The message does not exist.");
        }
        // Validate message text. Must not be blank or exceed 255 characters
        if (messageText == null || 
            messageText.isBlank() || 
            messageText.isEmpty() ||
            messageText.length() > 255){
            throw new IllegalArgumentException("Message cannot be blank or over 255 characters.");
        }
        // Get the existing message from the database
        Message updatedMessage = optionalMessage.get();
        // Update the message text with the new value
        updatedMessage.setMessageText(messageText);
        // Save the updated message back to the database
        messageRepository.save(updatedMessage);
        // Return the number of rows affected (1 in this case since one message is updated)
        return 1;
    }

    /**
     * Retrieves all messages posted by a specific user identified by the provided account ID.
     *
     * This method first checks if the account with the given accountId exists in the database
     * If the account does not exist, an IllegalArgumentException is thrown with a relevant message.
     * If the account exists, it retrieves all messages that have been posted by this account.
     *
     * @param accountId The unique identifier for the account whose messages are to be retrieved.
     *        This ID must correspond to an existing account in the system.
     * @return A list of Message objects representing all messages posted by the account with the specified ID.
     *         If no messages are found for the account, an empty list is returned.
     * @throws IllegalArgumentException If the account with the given accountId does not exist.
     */
    public List<Message> getAllMessagesFromUser(int accountId){
        // Ensure the account associated with the provided accountId exists
        if (!accountRepository.existsById(accountId)){
            throw new IllegalArgumentException("The account does not exist.");
        }
        return messageRepository.findAllMessagesByAccountId(accountId);
    }
}
