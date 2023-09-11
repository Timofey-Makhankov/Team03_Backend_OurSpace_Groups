package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractRepository;
import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


/**
 * This class creates the service for the Group Model
 * @since 1.0
 * @author Timofey
 */
@Service
public class GroupService extends AbstractServiceImpl<Group> {
    /**
     * The Default constructor for the GroupService class
     * @param repository that inherits from AbstractRepository
     * @since 1.0
     * @author Timofey
     */
    public GroupService(AbstractRepository<Group> repository) {
        super(repository);
    }
}
