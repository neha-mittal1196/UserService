package com.neha.UserService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neha.UserService.exceptions.ValidTokenNotFoundException;
import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;

public interface UserService {
    Token login(String email, String password);

    User signUp(String name, String email, String password) throws JsonProcessingException;

    void logout(String token) throws ValidTokenNotFoundException;

    User validateToken(String token) throws ValidTokenNotFoundException;
}