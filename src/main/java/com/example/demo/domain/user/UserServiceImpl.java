package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.group.GroupService;
import com.example.demo.domain.role.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.trace("trying to load User by username");
        return ((UserRepository) repository).findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User register(User user) {
        log.trace("trying to register a new user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var role = roleService.findByName("USER");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        var savedUser = save(user);
        log.debug("registered a new user");
        return savedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User registerUser(User user) {
        log.trace("trying to register a new user");
        user.setPassword(getRandomSpecialChars(20).toString());
        var role = roleService.findByName("USER");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        var savedUser = save(user);
        log.debug("registered a new user with random character");
        return savedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsersByGroupId(UUID id, Pageable pageable) {
        log.trace("trying to find all members from group");
        var result = ((UserRepository) repository).findAllByGroup_Id(id, pageable).getContent();
        log.debug("found all members from group");
        return result;
    }

    /**
     * Generates a stream of random characters from a count
     *
     * @param count amount of characters
     * @return a Stream of characters
     */
    public Stream<Character> getRandomSpecialChars(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        return specialChars.mapToObj(data -> (char) data);
    }

    @Override
    @Transactional
    public User updateById(UUID id, User entity) throws NoSuchElementException {
        log.trace("trying to update a user with id: " + id);
        var result = repository.findById(id).orElseThrow();
        entity.setPassword(result.getPassword());
        entity.setGroup(result.getGroup());
        var user = repository.save(entity);
        log.debug("updated user");
        return user;
    }
}
