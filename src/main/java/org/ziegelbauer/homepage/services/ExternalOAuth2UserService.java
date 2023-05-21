package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.authentication.ExternalOAuth2User;
import org.ziegelbauer.homepage.models.authentication.SimpleAuthority;
import org.ziegelbauer.homepage.models.authentication.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExternalOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        Optional<User> searchResult = userRepository.findByUsername(user.getAttribute("email"));

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(user.getAuthorities());

        if(searchResult.isPresent()) {
            if(searchResult.get().isAdmin()) {
                mappedAuthorities.add(new SimpleAuthority("admin"));
            }
        }

        return new ExternalOAuth2User(new DefaultOAuth2User(mappedAuthorities, user.getAttributes(), "name"));
    }
}
