package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.entity.Member;
import make.web.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@SessionAttributes("member_id")
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String main(Principal principal, Model model, HttpSession session) {
        log.info("홈 화면 이동");

        if(principal != null) {

            log.info("principal.getName() = {}", principal.getName());

            Member member = memberService.infoMember(principal.getName());
            Long id = member.getId();
            model.addAttribute("member_id", id);



        }
        return "main";
    }

    @GetMapping("/message")
    public String message(Model model) {
        return "message";
    }
}
