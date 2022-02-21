package make.web.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.member.dto.MemberFormDto;
import make.web.message.dto.MessageDto;
import make.web.message.dto.MessageSearchDto;
import make.web.message.dto.SendMessageDto;
import make.web.message.service.MessageService;
import make.web.member.entity.Member;
import make.web.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        MemberFormDto memberFormDto = memberService.getMember(email);
        Long id = memberFormDto.getId();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MessageDto> message = messageService.getMessageList(messageSearchDto, pageable, id);

        model.addAttribute("message", message);
        model.addAttribute("dto", messageSearchDto);
        model.addAttribute("maxPage", 5);

        return "/message/getMessageList";
    }

    @GetMapping("/message/send")
    public String sendMessageForm(@ModelAttribute("dto") SendMessageDto dto, Model model, Principal principal,
                                  HttpServletRequest request, HttpSession session) {

        String sendTo = request.getParameter("email");
        session.setAttribute("sendTo", sendTo);
        model.addAttribute("sendTo", sendTo);

        String sendFrom = principal.getName();
        model.addAttribute("sendFrom", sendFrom);

        return "message/sendMessage";
    }

    @PostMapping("/message/send")
    public String sendMessage(@Valid @ModelAttribute("dto") SendMessageDto dto, BindingResult bindingResult,
                              Principal principal, RedirectAttributes redirectAttributes,
                              Model model, HttpSession session) {

        if(bindingResult.hasErrors()) {
            String sendTo = (String) session.getAttribute("sendTo");
            model.addAttribute("sendTo", sendTo);

            String sendFrom = principal.getName();
            model.addAttribute("sendFrom", sendFrom);

            return "message/sendMessage";
        }

        try {
            Long messageId = messageService.sendMessage(dto);

            session.removeAttribute("sendTo");
            redirectAttributes.addFlashAttribute("msg", "메시지 전송이 완료되었습니다.");
            redirectAttributes.addFlashAttribute("url", "/");

        } catch (Exception e) {
            model.addAttribute("errorMsg", "메시지 전송이 실패했습니다.");
            return "message/sendMessage";
        }

        return "redirect:/alert";
    }

    @GetMapping("/message/{messageId}")
    public String getMessage(@PathVariable("messageId") Long messageId, Principal principal, Model model,
                             RedirectAttributes redirectAttributes) {

        try {
            MessageDto messageDto = messageService.readMessage(messageId);
            model.addAttribute("message", messageDto);
            model.addAttribute("sendTo", messageDto.getSendFrom()); //답장하기 기능을 위해 받은 사람으로 메시지 보내기 위해
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "메시지가 없습니다.");
            redirectAttributes.addFlashAttribute("url", "/message/list");

            return "redirect:/alert";
        }

        return "message/getMessage";

    }

}
