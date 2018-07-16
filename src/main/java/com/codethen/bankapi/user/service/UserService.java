package com.codethen.bankapi.user.service;

import com.codethen.bankapi.user.domain.errors.UserAlreadyExistsException;
import com.codethen.bankapi.user.domain.errors.UserNotExistsException;
import com.codethen.bankapi.user.domain.model.User;
import com.codethen.bankapi.user.domain.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Creates a new user.
     * @throws UserAlreadyExistsException if an account already exists with same username.
     */
    public void create(User user) {
        userRepository.create(user);
    }

    /**
     * Returns the {@link User} for the given username.
     * @throws UserNotExistsException if the user doesn't exist.
     */
    public User findByUsername(String username) {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotExistsException();
        }
        return user;
    }
}
