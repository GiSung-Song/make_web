package make.web.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MessageController {

    @GetMapping("/message/list")
    public String getMessageList(Principal principal, Model model) {

        log.info("쪽지함 가져오기");

        return null;
    }

}
