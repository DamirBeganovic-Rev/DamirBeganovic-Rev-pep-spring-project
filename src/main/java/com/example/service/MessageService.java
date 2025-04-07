package com.example.service;

import java.util.List;

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
        List<Message> allMessages = messageRepository.findAll();

        return allMessages;
    }
}
