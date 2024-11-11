package com.neha.UserService.repositories;

import com.neha.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    User save(User user); // upser

    Optional<User> findByEmail(String email);
}
