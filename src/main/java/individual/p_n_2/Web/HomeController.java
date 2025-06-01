package individual.p_n_2.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        // fullName w modelu leci dzięki GlobalModelAttributes
        return "home";
    }
}