package org.ziegelbauer.homepage.data;

import org.springframework.data.repository.CrudRepository;
import org.ziegelbauer.homepage.models.authentication.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
