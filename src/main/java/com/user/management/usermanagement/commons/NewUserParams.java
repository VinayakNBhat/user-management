package com.user.management.usermanagement.commons;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewUserParams {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String roles;
}
