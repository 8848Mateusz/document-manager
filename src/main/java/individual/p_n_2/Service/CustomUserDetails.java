package individual.p_n_2.Service;

import individual.p_n_2.Domain.User.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String email;
    private final String password;
    private final String fullName;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User u) {
        this.email       = u.getEmail();
        this.password    = u.getPassword();
        this.fullName    = u.getFullName();
        this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + u.getRole())
        );
    }

    public String getFullName() {
        return fullName;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword()                { return password; }
    @Override public String getUsername()                { return email; }
    @Override public boolean isAccountNonExpired()       { return true; }
    @Override public boolean isAccountNonLocked()        { return true; }
    @Override public boolean isCredentialsNonExpired()   { return true; }
    @Override public boolean isEnabled()                 { return true; }
}