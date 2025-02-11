package com.neha.UserService.dtos;

import com.neha.UserService.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenDto {
    private String value;
    private Date expiryAt;
    private String email;

    public static TokenDto from(Token token) {
        if (token == null) return null;

        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(token.getValue());
        tokenDto.setExpiryAt(token.getExpiryAt());
        tokenDto.setEmail(token.getUser().getEmail());

        return tokenDto;
    }
}
