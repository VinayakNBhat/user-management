package com.user.management.usermanagement.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String roles;
}
