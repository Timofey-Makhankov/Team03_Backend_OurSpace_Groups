package com.example.demo.domain.group;

import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.group.dto.GroupMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the REST Controller for the Group Model
 *
 * @author Timofey
 * @version 1.0
 * @see Group
 */
@RestController
@RequestMapping(path = "/group")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    /**
     * Return all groups from the database
     *
     * @return list of groups
     * @apiNote returns an empty list, if it wasn't able to find any groups
     * @see GroupDTO
     * @since 1.0
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAuthority('GROUP_READ_ALL')")
    @Operation(
            summary = "Get All Groups",
            description = "This Endpoint returns all the groups in the database. This requires an authenticated user" +
                    " using JWT token. This can be accessed with the authority 'group_read_all'",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok().body(groupMapper.toDTOs(groupService.findAll()));
    }

    /**
     * Return a group from the given Id
     *
     * @param id group id from path
     * @return a groupDTO
     * @apiNote A member can only see his own group. throws EntityNotFound exception, if the group wasn't found from the given id
     * @see GroupDTO
     * @since 1.0
     */
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('GROUP_READ') or @userPermissionEvaluator.isInGroup(authentication.principal.user, #id)")
    @Operation(
            summary = "Get a Group by id",
            description = "get a groupDTO by id. This requires an authenticated user using JWT token. This can be" +
                    " accessed by the authority 'group_read' or the client belongs to that group",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(groupMapper.toDTO(groupService.findById(id)));
    }

    /**
     * Create a new group model in the database
     *
     * @param group groupDTO information
     * @return newly created groupDTO from database
     * @apiNote throwsMethodArgumentNotValid exception if the validation in the DTO fails
     * @see GroupDTO
     * @since 1.0
     */
    @PostMapping
    @PreAuthorize("hasAuthority('GROUP_CREATE')")
    @Operation(
            summary = "Create a new group",
            description = "Create a new group. This requires an authenticated user using JWT token. It can only be" +
                    " accessed with the authority 'group_create'",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<GroupDTO> createGroup(@RequestBody @Valid GroupDTO group) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupMapper.toDTO(groupService.save(groupMapper.fromDTO(group))));
    }

    /**
     * Delete a group from given id
     *
     * @param id group id from path
     * @since 1.0
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('GROUP_DELETE')")
    @Operation(
            summary = "Delete a Group",
            description = "Delete a given Group by its id. This requires an authenticated user using JWT token. It can" +
                    " only be accessed with authority 'group_delete'",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public void deleteGroup(@PathVariable("id") UUID id) {
        groupService.deleteById(id);
    }

    /**
     * update a group with the given id
     *
     * @param id    group id from path
     * @param group groupDTO with the updated information
     * @see GroupDTO
     * @since 1.0
     */
    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('GROUP_MODIFY')")
    @Operation(
            summary = "Update a Group",
            description = "Update a Group by Id. This requires an authenticated user using JWT token. It can only be " +
                    "accessed with 'group_modify'",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable("id") UUID id, @Valid @RequestBody GroupDTO group) {
        return ResponseEntity.ok().body(groupMapper.toDTO(groupService.updateById(id, groupMapper.fromDTO(group))));
    }

    /**
     * Return an Error Response when EntityNotFound exception is thrown
     *
     * @param enfe exception
     * @return Response entity with the exception message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException enfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
    }

    /**
     * Return an Error Response when MethodArgumentNotValid exception is thrown
     *
     * @param e exception
     * @return Response entity with the default exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }
}
