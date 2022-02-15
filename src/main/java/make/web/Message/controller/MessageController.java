package make.web.Message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.Message.dto.MessageDto;
import make.web.Message.dto.MessageSearchDto;
import make.web.Message.service.MessageService;
import make.web.entity.Member;
import make.web.service.MemberService;
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
    private final MemberService memberService;

    @GetMapping(value = {"/message/list", "/message/list/{page}"})
    public String getMessageList(Principal principal, Model model, MessageSearchDto messageSearchDto,
                                 @PathVariable("page") Optional<Integer> page, MessageDto messageDto) {

        log.info("쪽지함 가져오기");

        String email = principal.getName();
        Member member = memberService.getMember(email);
        Long id = member.getId();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MessageDto> message = messageService.getMessageList(messageSearchDto, pageable, id);

        model.addAttribute("message", message);
        model.addAttribute("dto", messageSearchDto);
        model.addAttribute("maxPage", 5);

        return "/message/getMessageList";
    }

    @GetMapping("/message/send")
    public String readMessage(@PathVariable("messageId") Long messageId) {
        return null;
    }

}
