package individual.p_n_2.Web;

import individual.p_n_2.Domain.User;
import individual.p_n_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @Autowired
    UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName(); // <-- to jest e-mail użytkownika
        User user = userService.findByEmail(email); // <-- pobieramy cały obiekt User
        model.addAttribute("fullName", user.getFullName()); // <-- wyciągamy fullName
        return "dashboard";
    }
}
