package com.neha.UserService.controllers;

import com.neha.UserService.dtos.LoginRequestDto;
import com.neha.UserService.dtos.LogoutRequestDto;
import com.neha.UserService.dtos.SignupRequestDto;
import com.neha.UserService.dtos.UserDto;
import com.neha.UserService.exceptions.UserNotFoundException;
import com.neha.UserService.exceptions.ValidTokenNotFoundException;
import com.neha.UserService.models.Token;
import com.neha.UserService.models.User;
import com.neha.UserService.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserServiceImpl userServiceImpl;

    UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupRequestDto requestDto) {
        User user = userServiceImpl.signUp(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto) throws UserNotFoundException {
        return userServiceImpl.login(requestDto.getEmail(), requestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) throws ValidTokenNotFoundException {
        userServiceImpl.logout(requestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) throws ValidTokenNotFoundException {
        User user = userServiceImpl.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        System.out.println("Got the request here in UserService");
        return null;
    }
}
