package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.trace("trying to load User by username");
        return ((UserRepository) repository).findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public User register(User user) {
        log.trace("trying to register a new user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser =  save(user);
        log.debug("registered a new user");
        return savedUser;
    }

    @Override
    public User registerUser(User user) {
        log.trace("trying to register a new user");
        user.setPassword(getRandomSpecialChars(20).toString());
        var savedUser = save(user);
        log.debug("registered a new user with random character");
        return savedUser;
    }

    @Override
    public List<User> getAllUsersByGroupId(UUID id, Pageable pageable) {
        log.trace("trying to find all members from group");
        var result = ((UserRepository) repository).findAllByGroup_Id(id, pageable).getContent();
        log.debug("found all members from group");
        return result;
    }

    public Stream<Character> getRandomSpecialChars(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        return specialChars.mapToObj(data -> (char) data);
    }

}
