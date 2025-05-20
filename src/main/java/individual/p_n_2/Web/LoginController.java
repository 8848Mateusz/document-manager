package individual.p_n_2.Web;

import individual.p_n_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

        @GetMapping("/login")              // ← małe „l”, dokładnie tak samo jak w Security
        public String login(Authentication authentication) {

            // Zalogowany? → /dashboard
            if (authentication != null &&
                    authentication.isAuthenticated() &&
                    !(authentication instanceof AnonymousAuthenticationToken)) {

                return "redirect:/dashboard";
            }

            // Niezalogowany → pokaż formularz
            return "login";                // login.jsp
        }
    }



