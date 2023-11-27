package com.scub.recipies.repository;

import com.scub.recipies.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <User, Long> {
    Iterable<User> findUserByName(String name);

    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}
