package com.example.demo.domain.role;

import com.example.demo.core.generic.AbstractRepository;
import com.example.demo.core.generic.AbstractServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractServiceImpl<Role> {
    public RoleService(AbstractRepository<Role> repository) {
        super(repository);
    }

    /**
     * {@inheritDoc}
     */
    public Role findByName(String name) {
        return ((RoleRepository) repository).findByName(name);
    }
}
