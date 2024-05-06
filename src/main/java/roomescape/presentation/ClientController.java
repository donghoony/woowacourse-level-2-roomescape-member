package roomescape.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }
}
