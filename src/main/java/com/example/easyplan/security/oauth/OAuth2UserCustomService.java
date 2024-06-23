package com.example.easyplan.security.oauth;

import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("loadUser");

        OAuth2User oAuth2User1 = super.loadUser(userRequest);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oauth2UserService.loadUser(userRequest);

        Map<String, Object> attribute = oAuth2User.getAttributes();

        OAuthAttributes attributes = OAuthAttributes.of("kakao", oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes, userRequest.getAccessToken());

        log.info("user: " + user.getEmail());

        try {
            log.info(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attribute,
                attributes.getNameAttributesKey(),
                user.getEmail(),
                user.getRole().getKey());

        log.info("return: " + customOAuth2User.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return customOAuth2User;
    }

    private User saveOrUpdate(OAuthAttributes attributes, OAuth2AccessToken accessToken) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());

        user.setAccessToken(accessToken.getTokenValue());

        log.info(accessToken.getTokenValue());

        return userRepository.save(user);
    }
}
