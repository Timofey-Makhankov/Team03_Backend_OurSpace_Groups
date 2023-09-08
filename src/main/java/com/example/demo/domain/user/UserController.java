package com.example.demo.domain.user;

import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import com.example.demo.domain.user.dto.UserRegisterDTO;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @Autowired
  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @userPermissionEvaluator.isSameUSer(authentication.principal.user, id)")
  public ResponseEntity<UserDTO> retrieveById(@PathVariable UUID id) {
    User user = userService.findById(id);
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserDTO>> retrieveAll() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(userMapper.toDTOs(users), HttpStatus.OK);
  }

  @GetMapping("/group/{id}")
  @PreAuthorize("@userPermissionEvaluator.isInGroup(authentication.principal.user, #id)")
  public ResponseEntity<List<UserDTO>>getMembersFromGroupId(
          @PathVariable("id") UUID id,
          @RequestParam(value = "p") @NumberFormat Integer page,
          @RequestParam(value = "i") @NumberFormat Integer amount
  ) {
    Pageable pageable = PageRequest.of(page, amount);
    return ResponseEntity.ok().body(userMapper.toDTOs(userService.getAllUsersByGroupId(id, pageable)));
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
    User user = userService.register(userMapper.fromUserRegisterDTO(userRegisterDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }
  @PostMapping("/registerUser")
  public ResponseEntity<UserDTO> registerWithoutPassword(@Valid @RequestBody UserDTO userDTO) {
    User user = userService.registerUser(userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }
  @PutMapping("/{id}")
  @PreAuthorize(
      "hasAuthority('USER_MODIFY') or @userPermissionEvaluator.isSameUSer(authentication.principal.user, userDTO)")
  public ResponseEntity<UserDTO> updateById(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
    User user = userService.updateById(id, userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_DELETE') or @userPermissionEvaluator.isSameUSer(authentication.principal.user, id)")
  public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
    userService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
