package com.codethen.bankapi.user.repository;

import com.codethen.bankapi.user.domain.errors.UserAlreadyExistsException;
import com.codethen.bankapi.user.domain.model.User;
import com.codethen.bankapi.user.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> accounts = new HashMap<>();

    @Override
    public void create(User user) {
        if (accounts.containsKey(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        accounts.put(user.getUsername(), user);
    }

    @Override
    public User findByUsername(String username) {
        return accounts.get(username);
    }
}