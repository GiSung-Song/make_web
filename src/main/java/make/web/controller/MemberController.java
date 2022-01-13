package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.dto.FindIdFormDto;
import make.web.dto.FindPwFormDto;
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

import javax.persistence.EntityNotFoundException;
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
            Member member = Member.createMember(memberFormDto, passwordEncoder);
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

    @GetMapping("/findId")
    public String findIdForm(@ModelAttribute("member") FindIdFormDto findIdFormDto) {
        log.info("/member/findId 아이디 찾기로 이동");
        return "member/findMemberIdForm";
    }

    @PostMapping("/findId")
    public String findId(@Valid @ModelAttribute("member") FindIdFormDto findIdFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "member/findMemberIdForm";
        }

        try {
            Member member = memberService.findMemberId(findIdFormDto);
            model.addAttribute("findMail", member.getEmail());
        } catch (EntityNotFoundException e) {
            log.info("등록된 이메일이 없는 경우");
            model.addAttribute("errorMessage", e.getMessage());
            log.info(e.getMessage());
            return "member/findMemberIdForm";
        }

        return "/member/find/idForm";
    }

    @GetMapping("/findPw")
    public String findPwForm(@ModelAttribute("member") FindPwFormDto findPwFormDto) {
        log.info("/member/findPw 비밀번호 찾기로 이동");
        return "member/findMemberPwForm";
    }


    //이메일로 임시비밀번호 보내는 기능 구현해야 함.
    @PostMapping("/findPw")
    public String findPw(@Valid @ModelAttribute("member") FindPwFormDto findPwFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "member/findMemberPwForm";
        }

        try {
            Member member = memberService.findMemberPw(findPwFormDto);
        } catch (EntityNotFoundException e) {
            log.info("등록된 이메일이 없는 경우");
            model.addAttribute("errorMessage", e.getMessage());
            return "member/findMemberPwForm";
        }

        return "/member/find/pwForm";
    }


}
