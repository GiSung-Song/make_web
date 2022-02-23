package make.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.member.dto.*;
import make.web.member.entity.Member;
import make.web.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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

//    @PostMapping("/new")
    public String signUp(@Valid @ModelAttribute("member") MemberFormDto memberFormDto,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

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

        redirectAttributes.addFlashAttribute("msg", "회원가입이 성공하였습니다.");
        redirectAttributes.addFlashAttribute("url", "/member/login");

        return "redirect:/alert";
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
            IdFormDto idFormDto = memberService.findMemberId(findIdFormDto);
            String findMail = idFormDto.getEmail();
            model.addAttribute("findMail", findMail);
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

    @GetMapping("/info")
    public String infoMember(Principal principal, Model model) {
        log.info("회원 정보 페이지로 이동");

        try {
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);

            model.addAttribute("member", memberFormDto);

        } catch (EntityNotFoundException e) {
            log.info("error");

            return "/main";
        }

        return "/member/infoMemberForm";
    }

    @GetMapping("/edit")
    public String connectEdit(Model model, Principal principal) {
        MemberFormDto memberFormDto = memberService.getMember(principal.getName());

        EditFormDto editFormDto = new EditFormDto();

        editFormDto.setAddress(memberFormDto.getAddress());
        editFormDto.setEmail(memberFormDto.getEmail());
        editFormDto.setName(memberFormDto.getName());
        editFormDto.setPhone(memberFormDto.getPhone());

        model.addAttribute("member", editFormDto);

        return "member/editMemberForm";
    }

    @PostMapping("/edit")
    public String editMember(@Valid @ModelAttribute("member") EditFormDto editFormDto, BindingResult bindingResult,
                             Model model, Principal principal,  RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("Edit Form Error");
            return "member/editMemberForm";
        }

        try {
            memberService.editMember(principal.getName(), editFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "이미 존재하는 번호입니다.");
            return "member/editMemberForm";
        }

        log.info("Member 수정 완료");
        redirectAttributes.addFlashAttribute("msg", "정보 수정이 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/member/info");

        return "redirect:/alert";
    }

    @PostMapping("/new")
    public String registerMember(@Valid @ModelAttribute("member") MemberFormDto memberFormDto, HttpSession session,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "member/createMemberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            String key = memberService.checkMember(member);//중복이 없으면 키 생성
            member.addKey(key); //멤버에 setKey
            session.setAttribute("checking", member);
            memberService.sendAuth(member, member.getKey()); //메일 보내기

            log.info("key = {}", key);
            log.info("member.getKey() = {}", member.getKey());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/createMemberForm";
        }

        redirectAttributes.addFlashAttribute("msg", "메일로 인증번호를 전송했습니다.");
        redirectAttributes.addFlashAttribute("url", "/member/new/auth");

        return "redirect:/alert";
    }

    @GetMapping("/new/auth")
    public String confirmKey(@ModelAttribute("auth") AuthFormDto authFormDto) {
        return "/member/auth";
    }

    @PostMapping("/new/auth")
    public String checkKey(@ModelAttribute("auth") AuthFormDto authFormDto, HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Member member = (Member) session.getAttribute("checking");
        String key = member.getKey();

        log.info("member.getKey = {}", key);
        log.info("enter key = {}", authFormDto.getKey());

        if(!key.equals(authFormDto.getKey())) {
            redirectAttributes.addFlashAttribute("msg", "인증번호가 다릅니다.");
            redirectAttributes.addFlashAttribute("url", "/member/new/auth");

            return "redirect:/alert";
        }

        memberService.saveMember(member);

        redirectAttributes.addFlashAttribute("msg", "회원가입이 성공하였습니다.");
        redirectAttributes.addFlashAttribute("url", "/member/login");
        session.removeAttribute("checking");

        return "redirect:/alert";
    }

}
