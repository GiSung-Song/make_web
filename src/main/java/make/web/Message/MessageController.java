package make.web.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MessageController {

    private final MessageService messageService;

    @GetMapping(value = {"/message/list", "/message/list/{page}"})
    public String getMessageList(Principal principal, Model model,
                                 @PathVariable("page") Optional<Integer> page, MessageDto messageDto) {

        log.info("쪽지함 가져오기");

        String email = principal.getName();

        return "/message/getMessageList";
    }

}
