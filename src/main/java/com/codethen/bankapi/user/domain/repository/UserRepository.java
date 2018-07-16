package com.codethen.bankapi.user.domain.repository;

import com.codethen.bankapi.user.domain.errors.UserAlreadyExistsException;
import com.codethen.bankapi.user.domain.model.User;

public interface UserRepository {

    /**
     * Creates a new user.
     * @throws UserAlreadyExistsException if a user already exists with same username.
     */
    void create(User user);

    /**
     * Returns the {@link User} with the given username or null if it doesn't exist.
     */
    User findByUsername(String username);
}
