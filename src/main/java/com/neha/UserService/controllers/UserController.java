package com.neha.UserService.controllers;

import com.neha.UserService.dtos.LoginRequestDto;
import com.neha.UserService.dtos.LogoutRequestDto;
import com.neha.UserService.dtos.SignupRequestDto;
import com.neha.UserService.dtos.UserDto;
import com.neha.UserService.exceptions.UserNotFoundException;
import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;
import com.neha.UserService.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto) throws UserNotFoundException {
        return userService.login(requestDto.getEmail(), requestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) {
        userService.logout(requestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return null;
    }
}
