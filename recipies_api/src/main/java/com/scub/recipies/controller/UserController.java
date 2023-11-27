package com.scub.recipies.controller;

import com.scub.recipies.model.User;
import com.scub.recipies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Read - Get all the user
     *
     * @return an array of users
     */
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Read - Get a user by user_id
     *
     * @param id
     * @return A Users object fulfilled
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create - Create a new user
     *
     * @param user
     * @throws Exception
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Update - Update an existing user
     *
     * @param id
     * @param userDetails
     * @throws Exception
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") final Long id,
                                           @RequestBody User userDetails) throws Exception {
        Optional<User> currentUser = userService.getUserById(id);
        if (currentUser.isPresent()) {
            User userToUpdate = currentUser.get();
            userToUpdate.setName(userDetails.getName());
            userToUpdate.setEmail(userDetails.getEmail());
            userToUpdate.setPassword(userDetails.getPassword());

            User updatedUser = userService.saveUser(userToUpdate);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete - Delete an existing user
     *
     * @param id
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Read - Authorize a user to login
     *
     * @param loginUser
     * @param authToken
     */
    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody User loginUser, @RequestHeader(value = "Authorization", required = false) String authToken) {
        User user = userService.authenticate(loginUser.getEmail(), loginUser.getPassword());
        System.out.println("Token reçu: " + authToken);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("l'email ou le mot de passe sont invalide.");
        }
    }
}
