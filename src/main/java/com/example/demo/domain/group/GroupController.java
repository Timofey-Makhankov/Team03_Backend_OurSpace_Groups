package com.example.demo.domain.group;

import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.group.dto.GroupMapper;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserMapper userMapper;
    public GroupController(GroupService groupService, GroupMapper groupMapper, UserMapper userMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok().body(groupMapper.toDTOs(groupService.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(groupMapper.toDTO(groupService.findById(id)));
    }

    @GetMapping("/{id}/members/")
    public ResponseEntity<List<UserDTO>> getMembersFromGroupId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(userMapper.toDTOs(groupService.getAllUsersInGroup(id)));
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO newGroup) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupMapper.toDTO(groupService.save(groupMapper.fromDTO(newGroup))));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteGroup(@PathVariable("id") UUID id) {
        groupService.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    public void updateGroup(@PathVariable("id") UUID id, @RequestBody GroupDTO newGroup) {
        groupService.updateById(id, groupMapper.fromDTO(newGroup));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The Model was not able to be found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }
}
