package make.web.service;

import lombok.RequiredArgsConstructor;
import make.web.entity.Member;
import make.web.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

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

}
