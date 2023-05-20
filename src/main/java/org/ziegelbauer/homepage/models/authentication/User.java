package org.ziegelbauer.homepage.models.authentication;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String hashedPassword;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private boolean isAdmin;
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isAdmin)
        {
            return List.of(new SimpleAuthority("ROLE_ADMIN"));
        }
        return null;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
