package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.dto.MemberFormDto;
import make.web.entity.Member;
import make.web.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String addForm(@ModelAttribute("member") MemberFormDto memberFormDto) {
        log.info("/member/new 접속");
        return "member/createMemberForm";
    }

    @PostMapping("/new")
    public String signUp(@Valid @ModelAttribute("member") MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "member/createMemberForm";
        }

        try {
            Member member = Member.createUser(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            log.info("duplicate email");
            model.addAttribute("errorMessage", e.getMessage());
            return "member/createMemberForm";
        }

        log.info("create user success");

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        log.info("/member/login 로그인 페이지로 이동");
        return "/member/loginMemberForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        log.info("로그인 오류");
        model.addAttribute("errorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/loginMemberForm";
    }
}
