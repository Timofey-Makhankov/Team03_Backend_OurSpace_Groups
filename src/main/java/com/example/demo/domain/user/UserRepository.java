package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * This is the interface of the User Repository that Extends the AbstractRepository
 *
 * @author Timofey
 * @version 1.0
 * @see AbstractRepository
 */
@Repository
public interface UserRepository extends AbstractRepository<User> {
    /**
     * Get The User from the Database by its Email
     *
     * @param email string
     * @return Optional User from the Database
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all Members from a Group by the group Id
     *
     * @param group_id UUID
     * @param pageable Pageable instance
     * @return a Page of Type User from the Database
     */
    Page<User> findAllByGroup_Id(UUID group_id, Pageable pageable);
}
