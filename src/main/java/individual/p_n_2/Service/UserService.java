package individual.p_n_2.Service;

import individual.p_n_2.Dto.UserDto;
import individual.p_n_2.Domain.User;



public interface UserService {
    User saveUser(UserDto userDto);
    User findByEmail(String email);
    boolean isEmailAlreadyInUse(String email);
    boolean isFullNameAlreadyInUse(String fullName);
    boolean createResetToken(String email, String token);
    boolean isResetTokenValid(String token);
    boolean resetPassword(String token, String newPassword);
}
