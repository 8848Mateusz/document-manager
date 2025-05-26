package individual.p_n_2.Web;

import individual.p_n_2.Service.EmailService;
import individual.p_n_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        String token = UUID.randomUUID().toString();

        boolean result = userService.createResetToken(email, token);
        if (result) {
            model.addAttribute("message", "Na podany adres e-mail wysłano link do resetowania hasła.");
        } else {
            model.addAttribute("error", "Nie znaleziono konta o podanym adresie e-mail.");
        }
        return "forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        if (!userService.isResetTokenValid(token)) {
            model.addAttribute("error", "Invalid token.");
            return "resetPassword";
        }
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processReset(@RequestParam("token") String token,
                               @RequestParam("password") String password,
                               Model model) {
        boolean result = userService.resetPassword(token, password);
        if (result) {
            return "redirect:/login?resetSuccess";
        } else {
            model.addAttribute("error", "Could not reset password.");
            return "resetPassword";
        }
    }
}
