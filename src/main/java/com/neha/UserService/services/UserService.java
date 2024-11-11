package com.neha.UserService.services;

import com.neha.UserService.exceptions.UserNotFoundException;
import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;
import com.neha.UserService.repositories.TokenRepository;
import com.neha.UserService.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository, TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
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

    public Token login(String email, String password) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " doesnt exist");
        }

        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
           return null;
        }

        //Login successful, generate a token
        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        return token;
    }

    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);
        if(optionalToken.isEmpty()) {
            return;
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtAfter(token, false, new Date());
        if(optionalToken.isEmpty()) {
            return null;
        }
        return optionalToken.get().getUser();
    }
}
