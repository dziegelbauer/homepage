package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.data.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var searchResult = userRepository.findByUsername(username);
        if(searchResult.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return searchResult.get();
    }
}
