package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

/**
 * THis is the interface for UserService that extends The AbstractService
 *
 * @author Timofey
 * @version 1.0
 * @see AbstractService
 */
public interface UserService extends UserDetailsService, AbstractService<User> {
    /**
     * Register a User to the repository
     *
     * @param user User
     * @return the registered User from the repository
     */
    User register(User user);

    /**
     * Register a User to the repository with a random generated password
     *
     * @param user User
     * @return the registered User from the repository
     */
    User registerUser(User user);

    /**
     * Get all members from a group by the Group Id
     *
     * @param id       UUID group
     * @param pageable pageable instance
     * @return a list of users from a group
     */
    List<User> getAllUsersByGroupId(UUID id, Pageable pageable);
}
