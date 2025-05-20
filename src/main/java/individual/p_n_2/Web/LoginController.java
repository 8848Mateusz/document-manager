package individual.p_n_2.Web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class LoginController {

        @GetMapping("/login")              // ← małe „l”, dokładnie tak samo jak w Security
        public String login(Authentication authentication) {

            // Zalogowany? → /dashboard
            if (authentication != null &&
                    authentication.isAuthenticated() &&
                    !(authentication instanceof AnonymousAuthenticationToken)) {

                return "redirect:/dashboard";
            }

            // Niezalogowany → pokaż formularz
            return "login";
        }
    }



