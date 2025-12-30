package com.server.config;

import com.server.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                // 授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()

                        .requestMatchers("/h2/**").permitAll()

                        .anyRequest().authenticated()
                )

                // 先用最简单的方式（后面再换 JWT）
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}