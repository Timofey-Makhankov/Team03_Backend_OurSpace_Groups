package com.example.demo.core.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the abstract class implementation of the AbstractService<T> interface.
 * This is already defined the main functions of the interface
 *
 * @param <T> The Entity that extends AbstractEntity
 * @author Timofey
 * @see AbstractService
 * @since 1.0
 */
@Log4j2
@AllArgsConstructor
public abstract class AbstractServiceImpl<T extends AbstractEntity> implements AbstractService<T> {

    protected final AbstractRepository<T> repository;

    /**
     * Save the Entity to the repository
     *
     * @param entity that extends AbstractEntity
     * @return the saved business object from the repository
     * @since 1.0
     */
    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Deletes the Entity by its Id
     *
     * @param id of the Entity
     * @throws NoSuchElementException if the Entity cannot be found
     * @since 1.0
     */
    @Override
    @Transactional
    public void deleteById(UUID id) throws NoSuchElementException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id));
        }
    }

    /**
     * Update the Entity by Id
     *
     * @param id     of the entity
     * @param entity to be updated to
     * @return the updated entity from the repository
     * @throws NoSuchElementException if the Entity cannot be found
     * @since 1.0
     */
    @Override
    @Transactional
    public T updateById(UUID id, T entity) throws NoSuchElementException {
        if (repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        } else {
            throw new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id));
        }
    }

    /**
     * Get all entities from the repository
     *
     * @return List of Entities from the repository
     * @since 1.0
     */
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Get all entities from the repository with pagination
     *
     * @param pageable instance
     * @return a list of entities that are paged from pageable
     * @since 1.0
     */
    @Override
    public List<T> findAll(Pageable pageable) {
        Page<T> pagedResult = repository.findAll(pageable);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    /**
     * Get a single entity from the repository with the given Id
     *
     * @param id from Entity
     * @return the found entity from the repository
     * @throws NoSuchElementException If the Entity cannot be found
     * @since 1.0
     */
    @Override
    public T findById(UUID id) {
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Check If the given entity is in the repository
     *
     * @param id from Entity
     * @return true, if the entity was found else return false$
     * @since 1.0
     */
    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
