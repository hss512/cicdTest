package com.example.easyplan.security.config;

import com.example.easyplan.repository.token.RefreshTokenRepository;
import com.example.easyplan.security.jwt.TokenAuthenticationFilter;
import com.example.easyplan.security.jwt.TokenProvider;
import com.example.easyplan.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.easyplan.security.oauth.OAuth2SuccessHandler;
import com.example.easyplan.security.oauth.OAuth2UserCustomService;
import com.example.easyplan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/js/**", "/ws/**");
    }

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigure -> corsConfigure.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                                .anyRequest().permitAll())
                .oauth2Login(login ->
                        login
                                .loginPage("/login")
                                .authorizationEndpoint(point -> point.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                                .successHandler(oAuth2SuccessHandler())
                                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserCustomService)))
                .logout(config -> config.logoutSuccessUrl("/"))
                .exceptionHandling(handler ->
                        handler
                                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"))
                );


        return http.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

}
