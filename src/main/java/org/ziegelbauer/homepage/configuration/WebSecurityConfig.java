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
import org.ziegelbauer.homepage.services.ExternalOAuth2LoginSuccessHandler;
import org.ziegelbauer.homepage.services.ExternalOAuth2UserService;
import org.ziegelbauer.homepage.services.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${app.login.redirect}")
    private String loginRedirect;

    private final ExternalOAuth2UserService oAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService users) throws Exception {
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
                        .requestMatchers("/oauth2/**")
                            .permitAll()
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
                .oauth2Login()
                    .loginPage("/auth/login")
                    .userInfoEndpoint()
                        .userService(oAuth2UserService)
                    .and()
                    .successHandler(new ExternalOAuth2LoginSuccessHandler(users))
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//    }
//
//    private ClientRegistration googleClientRegistration() {
//        return ClientRegistration.withRegistrationId("google")
//                .clientId("google-client-id")
//                .clientSecret("google-client-secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("openid", "profile", "email", "address", "phone")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .clientName("Google")
//                .build();
//    }
}
