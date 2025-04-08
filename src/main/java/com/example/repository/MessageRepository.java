package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

    /**
     * Retrieves all messages posted by a specific account, identified by the given accountId.
     *
     * This method performs a query to find all Message entities in the database where the 
     * postedBy field (which represents the account ID of the user who posted the message) 
     * matches the provided accountId.
     *
     * @param accountId The ID of the account whose messages are to be retrieved.
     *        This corresponds to the postedBy field in the Message entity.
     * @return A list of Message entities associated with the specified account. 
     *         If no messages are found for the given account, an empty list is returned.
     * 
     */
    @Query("SELECT m FROM Message m WHERE m.postedBy = ?1")
    public List<Message> findAllMessagesByAccountId(int accountId);

}
