package individual.p_n_2.Service;

import individual.p_n_2.Domain.User.User;
import individual.p_n_2.Repository.User.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepo.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Nie znaleziono użytkownika o e-mailu: " + email)
                );
    }
}