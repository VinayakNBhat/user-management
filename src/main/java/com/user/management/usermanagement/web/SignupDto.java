package com.user.management.usermanagement.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SignupDto {
    @NotNull
    private String userName;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    private String roles;

    protected SignupDto() {}

    public SignupDto(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }

    public SignupDto(String userName, String password, String firstName, String lastName) {
        this(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
