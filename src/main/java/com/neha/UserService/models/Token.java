package com.neha.UserService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseModel {

    private String value;
    @ManyToOne
    private User user;
    private Date expiryAt;

}
