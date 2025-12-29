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
                // 先关 CSRF（前后端分离必关）
                .csrf(AbstractHttpConfigurer::disable)

                // 授权规则
                .authorizeHttpRequests(auth -> auth
                        // ✅ 放行登录 & 注册
                        .requestMatchers("/login", "/register").permitAll()

                        // （可选）放行 H2 控制台
                        .requestMatchers("/h2/**").permitAll()

                        // 其他接口必须登录
                        .anyRequest().authenticated()
                )

                // 先用最简单的方式（后面再换 JWT）
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}