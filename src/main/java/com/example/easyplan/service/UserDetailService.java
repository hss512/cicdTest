package com.example.easyplan.service;

import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        log.info("UserDetailService 호출");
        return userRepository.findByEmail(email).orElseThrow();
    }
}
