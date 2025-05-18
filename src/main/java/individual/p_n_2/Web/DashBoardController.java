package individual.p_n_2.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @GetMapping("dashboard")
    public String HomeController() {

        return "dashboard";
    }
}
