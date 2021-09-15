package com.user.management.usermanagement.commons;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Builder
@Getter
@Setter
public class ModifyUserParams {
    private Optional<String> password;
    private Optional<String> roles;
}
