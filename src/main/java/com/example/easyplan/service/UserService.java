package com.example.easyplan.service;

import com.example.easyplan.domain.entity.user.UserRequest;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByUserKey(String userKey) {
        return userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }

    public User findByName(String userName) {
        return userRepository.findByName(userName)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }
}