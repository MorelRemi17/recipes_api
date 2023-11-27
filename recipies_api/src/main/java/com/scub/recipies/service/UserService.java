package com.scub.recipies.service;

import com.scub.recipies.model.User;
import com.scub.recipies.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) throws Exception {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isPresent()) {
            throw new Exception("L'email est déjà utilisé par un autre utilisateur.");
        }
        return userRepository.save(user);
    }

    public void deleteUserById(final Long id) {
        userRepository.deleteById(id);
    }

    public Iterable<User> getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public User authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
