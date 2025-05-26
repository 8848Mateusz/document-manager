package individual.p_n_2.Service;


import individual.p_n_2.Domain.PasswordResetToken;
import individual.p_n_2.Dto.UserDto;
import individual.p_n_2.Repository.PasswordResetTokenRepository;
import individual.p_n_2.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import individual.p_n_2.Domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;


    @Override
    public User saveUser(UserDto userDto) {
        if (isEmailAlreadyInUse(userDto.getEmail())) {
            throw new IllegalArgumentException("Email jest już zajęty");
        }

        if (isFullNameAlreadyInUse(userDto.getFullName())) {
            throw new IllegalArgumentException("Imię i nazwisko jest już zajęte");
        }

        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public boolean isEmailAlreadyInUse(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isFullNameAlreadyInUse(String fullName) {
        return userRepository.findByFullName(fullName) != null;
    }

    @Transactional
    @Override
    public boolean createResetToken(String email, String token) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        tokenRepository.deleteByUser(user);
        tokenRepository.flush();

        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/resetPassword?token=" + token;
        emailService.sendResetEmail(email, resetLink);

        return true;
    }

    @Override
    public boolean isResetTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) return false;

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) return false;

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
        return true;
    }

}