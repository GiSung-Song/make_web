package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main(Principal principal, Model model) {
        log.info("홈 화면 이동");
        return "main";
    }

    @GetMapping("/message")
    public String message(Model model) {
        return "message";
    }
}
