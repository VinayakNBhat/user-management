package com.user.management.usermanagement.commons.converter;

import com.user.management.usermanagement.domain.User;
import com.user.management.usermanagement.web.UserDto;

public class UserConverter {
    public static UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .userName(user.getUserName())
                .roles(user.getRoles())
                .id(user.getId())
                .build();
    }
}
