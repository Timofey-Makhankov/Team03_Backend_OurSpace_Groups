package com.example.demo.domain.user;

import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import com.example.demo.domain.user.dto.UserRegisterDTO;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * This is the REST Controller for the User Model
 *
 * @author Timofey
 * @version 1.0
 */
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

    /**
     * Return a User from the given id
     *
     * @param id from the Path
     * @return a UserDTO using a mapper from User
     * @apiNote The id has to be a UUID,otherwise it will return an error and throw MethodArgumentNotValidException
     * @see UserDTO
     * @since 1.0
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') or @userPermissionEvaluator.isSameUser(authentication.principal.user, #id)")
    @Operation(
            summary = "Get User by Id",
            description = "Get a User by its id. This requires an authenticated user using JWT token. It can only be" +
                    " accessed with authority 'user_read' or it belongs to the user that was requested by",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<UserDTO> retrieveById(@Valid @PathVariable UUID id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
    }

    /**
     * Return all the users
     *
     * @return a List of UserDTO
     * @apiNote This will return a List of all Users in the database, otherwise it will return an empty list
     * @see UserDTO
     * @since 1.0
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(
            summary = "Get all users",
            description = "Get a list of users. This requires an authenticated user using JWT token. It can only be " +
                    "accessed with the authority 'user_read'",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<List<UserDTO>> retrieveAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(userMapper.toDTOs(users), HttpStatus.OK);
    }

    /**
     * Return a list of all users from a group with a given id
     *
     * @param id     from group
     * @param page   number of the current page
     * @param amount the amount of items of the list
     * @return a List of UserDTO from the page number and count
     * @apiNote The page index starts at 0 and if the page is larger than the given members in repository, it will
     * return an empty list. If the amount id bigger than the returned amount, it will only return the amount that it
     * got. A Normal User can access this endpoint, if they belong to that group. This uses Request parameters. <b>p</b>
     * stands for "page" and <b>i</b> for "amount"
     * @see UserDTO
     * @since 1.0
     */
    @GetMapping("/group/{id}")
    @PreAuthorize("hasAuthority('GROUP_READ') or @userPermissionEvaluator.isInGroup(authentication.principal.user, #id)")
    @Operation(
            summary = "Get all members from a Group with group id",
            description = "This returns a list of users from a group by its id. This requires an authenticated user " +
                    "using JWT token. It requires parameters as well. It can only be accessed with the authority " +
                    "'group_read' or a user from that group",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<List<UserDTO>> getMembersFromGroupId(
            @PathVariable("id") UUID id,
            @RequestParam(value = "p") @NumberFormat @PositiveOrZero Integer page,
            @RequestParam(value = "i") @NumberFormat @Positive Integer amount
    ) {
        Pageable pageable = PageRequest.of(page, amount);
        return ResponseEntity.ok().body(userMapper.toDTOs(userService.getAllUsersByGroupId(id, pageable)));
    }

    /**
     * Register a new User to the database
     *
     * @param userRegisterDTO Details for the user
     * @return the new created User
     * @see UserRegisterDTO
     * @see UserDTO
     * @since 1.0
     */
    @PostMapping("/register")
    @Operation(
            summary = "Register a new User",
            description = "Register a new User. This does not require any authentication"
    )
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userService.register(userMapper.fromUserRegisterDTO(userRegisterDTO));
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
    }

    /**
     * Register a new User to the database with a random generated password
     *
     * @param userDTO User Information
     * @return created User with newly generated password
     * @see UserDTO
     * @since 1.0
     */
    @PostMapping("/registerUser")
    @Operation(
            summary = "Register a new User with password",
            description = "Register a new User with a newly generated password. This does not require any authentication"
    )
    public ResponseEntity<UserDTO> registerWithoutPassword(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userMapper.fromDTO(userDTO));
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
    }

    /**
     * Update a given User with an id
     *
     * @param id      User in path
     * @param userDTO The information to be updated to
     * @return the updated UserDTO
     * @apiNote The normal User can only access his own information only
     * @see UserDTO
     * @since 1.0
     */
    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAuthority('USER_MODIFY') or @userPermissionEvaluator.isSameUser(authentication.principal.user, #userDTO)")
    @Operation(
            summary = "Update a User",
            description = "Update a User with a given id. This requires an authenticated user using JWT token. This can" +
                    " only be accessed with the authority 'user_modify' or by the same user who requested it",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<UserDTO> updateById(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        User user = userService.updateById(id, userMapper.fromDTO(userDTO));
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
    }

    /**
     * Delete a User from given id
     *
     * @param id User in path
     * @return an empty Response
     * @apiNote a normal User can only delete his own account
     * @since 1.0
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE') or @userPermissionEvaluator.isSameUser(authentication.principal.user, #id)")
    @Operation(
            summary = "Delete a User",
            description = "Delete a User with the given id. This requires an authenticated user using JWT token. This " +
                    "can only be accessed with the authority 'user_delete' or by the same user, who requested it",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * This Exception handler will run, when the MethodArgumentNotValid Exception is thrown
     *
     * @param e Exception
     * @return an Error Response Entity with message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    /**
     * This Exception handler will run, when the ConstraintViolationException Exception is thrown
     *
     * @param e Exception
     * @return an Error Response Entity with message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage().replace("getMembersFromGroupId.", ""));
    }
}
