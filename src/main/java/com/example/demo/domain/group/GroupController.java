package com.example.demo.domain.group;

import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.group.dto.GroupMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/group")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GROUP_READ_ALL')")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok().body(groupMapper.toDTOs(groupService.findAll()));
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('GROUP_READ') or @userPermissionEvaluator.isInGroup(authentication.principal.user, id)")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(groupMapper.toDTO(groupService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GROUP_CREATE')")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO newGroup) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupMapper.toDTO(groupService.save(groupMapper.fromDTO(newGroup))));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('GROUP_DELETE')")
    public void deleteGroup(@PathVariable("id") UUID id) {
        groupService.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('GROUP_MODIFY')")
    public void updateGroup(@PathVariable("id") UUID id, @RequestBody GroupDTO newGroup) {
        groupService.updateById(id, groupMapper.fromDTO(newGroup));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException enfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }
}
