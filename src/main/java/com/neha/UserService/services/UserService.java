package com.neha.UserService.services;

import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;
import com.neha.UserService.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public User signUp(String email,
                       String name,
                       String password) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        //save the user object to the DB.
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        return null;
    }

    private Token generateToken(User user) {
        return null;
    }

    public void logout(String tokenValue) {
    }

    public User validateToken(String token) {
        return null;
    }
}
