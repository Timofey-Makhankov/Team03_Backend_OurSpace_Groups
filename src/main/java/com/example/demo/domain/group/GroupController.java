package com.example.demo.domain.group;

import org.springframework.beans.factory.annotation.Autowired;
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

    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok().body(groupService.findAll());
    }


    @GetMapping(path = "/{uuid}")
    public ResponseEntity<Group> getGroupById(@PathVariable("uuid") UUID id) throws GroupNotFoundException{
        return ResponseEntity.ok().body(groupService.findById(id));
    }

    @PostMapping
    public void postTank(@RequestBody Group newGroup) {
        groupService.save(newGroup);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteTank(@PathVariable("uuid") UUID id) throws GroupNotFoundException {
        groupService.deleteById(id);
    }

    @PutMapping(path = "/{uuid}")
    public void updateTank(@PathVariable("uuid") UUID id, @RequestBody Group newGroup) throws GroupNotFoundException {
        groupService.updateById(id, newGroup);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<String> handleTankNotFoundException(GroupNotFoundException e) {
        return ResponseEntity.status(404).body("Group not found.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }
}
