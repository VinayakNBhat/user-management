package com.user.management.usermanagement.controller;

import com.user.management.usermanagement.commons.ModifyUserParams;
import com.user.management.usermanagement.commons.NewUserParams;
import com.user.management.usermanagement.domain.User;
import com.user.management.usermanagement.service.UserService;
import com.user.management.usermanagement.web.LoginDto;
import com.user.management.usermanagement.web.SignupDto;
import com.user.management.usermanagement.web.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid SignupDto signupDto) {
        userService.createUser(NewUserParams.builder()
                .firstName(signupDto.getFirstName())
                .lastName(signupDto.getLastName())
                .password(signupDto.getPassword())
                .roles(signupDto.getRoles())
                .userName(signupDto.getUserName())
                .build());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void modifyUser(@PathVariable(value = "id") long id,
                           @RequestBody @Valid ModifyUserParams params) {
        userService.modifyUser(id, params);
    }

    @GetMapping
    public List<UserDto> listUsers() {
        return userService.listUser();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable(value = "id") long id) {
        User user = userService.getUser(id);
        return new UserDto(id, user.getUserName(), user.getRoles());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/signin")
    public String createJWT(@RequestBody LoginDto loginDto) {
        return userService.createJwt(loginDto.getUserName(), loginDto.getPassword())
                .orElseThrow(() -> new RuntimeException("Could not create JWT."));
    }
}
