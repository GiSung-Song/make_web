package make.web.service;

import lombok.RequiredArgsConstructor;
import make.web.entity.Member;
import make.web.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //중복 체크
    private void checkDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
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

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
