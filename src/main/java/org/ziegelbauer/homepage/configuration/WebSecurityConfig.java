package org.ziegelbauer.homepage.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${app.login.redirect}")
    private String loginRedirect;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                        .requestMatchers("/api/**")
                            .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/actuator/**")
                            .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/users", "/users/**")
                            .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/blogs/create", "/blogs/manage", "/cats/upload")
                            .hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .defaultSuccessUrl(loginRedirect, true)
                    .permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/auth/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
