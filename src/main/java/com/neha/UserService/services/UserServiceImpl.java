package com.neha.UserService.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neha.UserService.exceptions.UserNotFoundException;
import com.neha.UserService.exceptions.ValidTokenNotFoundException;
import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;
import com.neha.UserService.repositories.TokenRepository;
import com.neha.UserService.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    //private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
                    UserRepository userRepository,
                    TokenRepository tokenRepository,
                    ObjectMapper objectMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }

    public User signUp(String email,
                       String name,
                       String password) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        //        SendEmailDto sendEmailDto = new SendEmailDto();
//        sendEmailDto.setTo(email);
//        sendEmailDto.setSubject("Welcome to Scaler!!!");
//        sendEmailDto.setBody("We are happy to have you onboarded, All the best for your journey.");

        //Publish an event inside Kafka to send a Welcome Email to the user.
//        kafkaTemplate.send(
//                "sendEmail",
//                objectMapper.writeValueAsString(sendEmailDto)
//        );

        //save the user object to the DB.
        return userRepository.save(user);
    }

    public Token login(String email, String password)  {
         /* Github Copilot
        1. Find the user by email.
        2. If the user is not found, return null.
        3. If user is present in DB then verify the password.
        4. If password matches, then generate the token and return it.
         */

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        //Get all the tokens for this userId, and check the count.
        //TODO: Implement this.

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
        token.setValue(RandomStringUtils.randomAlphanumeric(128));//RandomStringUtils - coming from apache commons lang3
        return token;
    }

    public void logout(String tokenValue) throws ValidTokenNotFoundException {
        //We will be able to logout a particular token
        //if token is present in the DB, it's expiry time is greater than current time.
        //and deleted is false.

        Optional<Token> optionalToken =
                tokenRepository.findByValueAndDeletedAndExpiryAtAfter(tokenValue, false, new Date());

        if (optionalToken.isEmpty()) {
            throw new ValidTokenNotFoundException("Valid token not found.");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token) throws ValidTokenNotFoundException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtAfter(token, false, new Date());
        if(optionalToken.isEmpty()) {
            throw new ValidTokenNotFoundException("Valid token not found.");
        }
        return optionalToken.get().getUser();
    }
}
