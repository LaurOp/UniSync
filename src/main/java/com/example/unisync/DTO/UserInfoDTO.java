package com.example.unisync.DTO;

import com.example.unisync.Exception.Validation.EmailConstraint;
import com.example.unisync.Exception.Validation.PasswordConstraint;
import com.example.unisync.Exception.Validation.UsernameConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO extends BaseDto{

    @UsernameConstraint
    private String name;
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;

    private String roles;
}
