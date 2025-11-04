package com.example.server.user.service;

import com.example.server.user.domain.User;

public interface UserService {

    User register(String userId, String password, String name, Integer age);

    User authenticate(String userId, String password);
}
