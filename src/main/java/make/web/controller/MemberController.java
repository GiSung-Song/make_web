package make.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Ex {

    @GetMapping("/ex")
    public String test() {
        return "main";
    }
}
