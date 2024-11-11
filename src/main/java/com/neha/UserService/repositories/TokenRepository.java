package com.neha.UserService.repositories;

import com.neha.UserService.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Override
    Token save(Token token);

    Optional<Token> findByValueAndDeleted(String tokenValue, boolean deleted);

    Optional<Token> findByValueAndDeletedAndExpiryAtAfter(String tokenValue, boolean deleted, Date currentTime);
}
