package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User> {
  Optional<User> findByEmail(String email);
  Page<User> findAllByGroup_Id(UUID group_id, Pageable pageable);
}
