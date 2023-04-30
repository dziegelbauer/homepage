package org.ziegelbauer.homepage.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
public final class SimpleAuthority implements GrantedAuthority {
    private final String role;
    @Override
    public String getAuthority() {
        return role;
    }
}
