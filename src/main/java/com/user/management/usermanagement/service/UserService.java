package com.user.management.usermanagement.service;

import com.user.management.usermanagement.commons.ModifyUserParams;
import com.user.management.usermanagement.commons.NewUserParams;
import com.user.management.usermanagement.commons.converter.UserConverter;
import com.user.management.usermanagement.domain.User;
import com.user.management.usermanagement.repositories.UserRepository;
import com.user.management.usermanagement.security.JwtProvider;
import com.user.management.usermanagement.web.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private JwtProvider jwtProvider;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       JwtProvider jwtProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(NewUserParams params) throws RestClientException {
        LOGGER.info("Checking if the user already exists...");
        Optional<User> userOptional = userRepository.findByUserName(params.getUserName());
        if (userOptional.isPresent()) {
            final String error = String.format("User with the %s userName already exists.", params.getUserName());
            LOGGER.error(error);
            throw new RestClientException(error);
        }
        userRepository.save(new User(params.getUserName(),
                passwordEncoder.encode(params.getPassword()),
                params.getRoles(),
                params.getFirstName(),
                params.getLastName()));
        LOGGER.info("User successfully created.");
    }

    public UserDto modifyUser(long id, ModifyUserParams params) {
        LOGGER.info("Modifying user.");
        User user = userRepository.getById(id);

        if (params.getPassword().isPresent()) {
            user = user.withPassword(params.getPassword().get());
        }

        if (params.getRoles().isPresent()) {
            user = user.withRoles(params.getRoles().get());
        }

        userRepository.save(user);

        return UserConverter.convertToUserDto(getUser(id));
    }

    public List<UserDto> listUser() {
        return userRepository.findAll().stream().map(user -> UserDto.builder().roles(user.getRoles()).id(user.getId()).userName(user.getUserName()).build()).collect(Collectors.toList());
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RestClientException("User does not exist."));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public Optional<String> createJwt(String userName, String password) {
        LOGGER.info("Attempting to issue new JWT");
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUserName(userName);
        if (!user.isPresent()) {
            LOGGER.error("User does not exist.");
            throw new RuntimeException("User does not exist.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            token = Optional.of(jwtProvider.createToken(userName, Arrays.asList(user.get().getRoles().split("\\,"))));
        } catch (AuthenticationException e) {
            LOGGER.error(e.getMessage());
        }

        return token;
    }

    public void verifyUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User does not exist."));
    }
}
