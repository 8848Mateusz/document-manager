package individual.p_n_2.Web;

import individual.p_n_2.Service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("fullName")
    public String fullName(@AuthenticationPrincipal CustomUserDetails cud) {
        return (cud != null) ? cud.getFullName() : "Gość";
    }
}
