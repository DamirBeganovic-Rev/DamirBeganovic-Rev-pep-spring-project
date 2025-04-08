package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    /**
     * Retrieves an Account entity based on the provided username
     * 
     * This method performs a query to find an Account entity in the database where
     * the username matches the provided value.
     * 
     * @param username The username of the account to be retrieved.
     * @return An Optional<Account> containing the found account if it exists, or an empty
     *         Optional if no account with a matching username is found.
     */
    @Query
    Optional<Account> findByUsername(String username);

    /**
     * Retrieves an Account entity based on the provided username and password.
     * 
     * This method performs a query to find an Account entity where both the username and password 
     * match the provided values. This method is used for logging a user in.
     * @param username The username of the account to be retrieved
     * @param password The password of the account to be retrieved
     * @return An Optional<Account> containing the found account if the username and password match,
     *         or an empty Optional if no account is found with the provided username and password.
     */
    @Query
    Optional<Account> findByUsernameAndPassword(String username, String password);

}
