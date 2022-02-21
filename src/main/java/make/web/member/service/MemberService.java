package make.web.member.service;

import lombok.RequiredArgsConstructor;
import make.web.etc.config.CustomUserDetails;
import make.web.member.dto.*;
import make.web.member.entity.Member;
import make.web.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //회원 가입
    public Member saveMember(Member member) {
        checkDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void checkDuplicateMember(Member member) {
        Member phoneMember = memberRepository.findByPhone(member.getPhone());

        if(phoneMember != null) {
            throw new IllegalStateException("이미 가입된 번호입니다.");
        }

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

    public Long editMember(String email, EditFormDto editFormDto){
        Member member = memberRepository.findByEmail(email);

        Member savedMember = memberRepository.findByPhone(editFormDto.getPhone());

        if(savedMember != null) {
            throw new IllegalStateException("등록된 번호가 있습니다.");
        }

        member.editMember(editFormDto);

        return member.getId();
    }

}
