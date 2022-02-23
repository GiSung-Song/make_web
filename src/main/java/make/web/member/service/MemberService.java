package make.web.member.service;

import lombok.RequiredArgsConstructor;
import make.web.etc.config.CustomUserDetails;
import make.web.member.dto.*;
import make.web.member.entity.Member;
import make.web.member.repository.MemberRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * UserDetailService 인터페이스는 데이터베이스에서 회원 정보를 가져오는 역할을 함.
 * loadUserByUsername() 메소드는 회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환함.
 * 스프링 시큐리티에서 UserDetailService를 구현하고 있는 클래스를 통해 로그인 기능 구현
 */

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    //회원 가입
    public Member saveMember(Member member) {
        checkDuplicatePhone(member);
        checkDuplicateEmail(member);

        return memberRepository.save(member);
    }

    private void checkDuplicatePhone(Member member) {

        Member phoneMember = memberRepository.findByPhone(member.getPhone());

        if(phoneMember != null) {
            throw new IllegalStateException("이미 가입된 번호입니다.");
        }
    }

    private void checkDuplicateEmail(Member member) {
        Member emailMember = memberRepository.findByEmail(member.getEmail());

        if(emailMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

    }

    /**
     * UserDetail을 구현하고 있는 User 객체를 반환
     * User 객체를 생성하기 위해 회원 이메일, 비밀번호, role을 파라미터로 넘겨줌
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        return new CustomUserDetails(member);
    }

    @Transactional(readOnly = true)
    public IdFormDto findMemberId(FindIdFormDto findIdFormDto) {

//        String phone = findIdFormDto.getPhone().replaceAll("-", "");

        Member member = memberRepository.findByNameAndPhone(findIdFormDto.getName(), findIdFormDto.getPhone());

        if(member == null) {
            throw new EntityNotFoundException("등록된 이메일이 없습니다.");
        } else {
            IdFormDto idFormDto = new IdFormDto();
            idFormDto.setEmail(member.getEmail());

            return idFormDto;
        }

    }

    @Transactional(readOnly = true)
    public Member findMemberPw(FindPwFormDto findPwFormDto) {

//        String phone = findPwFormDto.getPhone().replaceAll("-", "");

        Member member = memberRepository.findByNameAndPhoneAndEmail(findPwFormDto.getName(), findPwFormDto.getPhone(), findPwFormDto.getEmail());

        if(member == null) {
            throw new EntityNotFoundException("등록된 회원이 없습니다.");
        } else
            return member;
    }

    @Transactional(readOnly = true)
    public MemberFormDto getMember(String email) {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new EntityNotFoundException("등록된 이메일이 없습니다.");
        } else {
            MemberFormDto memberFormDto = new MemberFormDto();

            memberFormDto.setEmail(member.getEmail());
            memberFormDto.setId(member.getId());
            memberFormDto.setName(member.getName());
            memberFormDto.setPhone(member.getPhone());
            memberFormDto.setAddress(member.getAddress());

            return memberFormDto;
        }
    }

    // 멤버 정보 수정(핸드폰 번호, 주소)
    public Long editMember(String email, EditFormDto editFormDto){
        Member member = memberRepository.findByEmail(email);

        Member savedMember = memberRepository.findByPhone(editFormDto.getPhone());

        if(savedMember != null) {
            throw new IllegalStateException("등록된 번호가 있습니다.");
        }

        member.editMember(editFormDto);

        return member.getId();
    }

    //메일 생성하기
    private MimeMessage createMessage(Member member, String key) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        String email = member.getEmail();

        message.addRecipients(Message.RecipientType.TO, email); //보내는 대상
        message.setSubject("인증번호가 도착했습니다."); //제목

        message.setText(key, "utf-8");
        message.setFrom(new InternetAddress("이메일 적기", "Test 인증"));

        return message;
    }

    //메일 보내기
    public void sendAuth(Member member, String key) throws Exception {
        checkDuplicateEmail(member);
        checkDuplicatePhone(member);
        MimeMessage message = createMessage(member, key);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new IllegalArgumentException();
        }
    }
}
