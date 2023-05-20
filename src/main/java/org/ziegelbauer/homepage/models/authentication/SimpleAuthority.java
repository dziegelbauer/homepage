package org.ziegelbauer.homepage.models.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public final class SimpleAuthority implements GrantedAuthority {
    private final String role;
    @Override
    public String getAuthority() {
        return role;
    }
}
