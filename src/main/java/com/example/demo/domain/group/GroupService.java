package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractRepository;
import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GroupService extends AbstractServiceImpl<Group> {

    public GroupService(AbstractRepository<Group> repository) {
        super(repository);
    }


    public List<User> getAllUsersInGroup(UUID id) {
        Group group = repository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id)));
        return group.getUsers();
    }
}
