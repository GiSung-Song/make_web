package make.web.service;

import make.web.dto.MemberFormDto;
import make.web.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Member ex_dto() {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@test.test")
                .name("테스트")
                .password("비밀번호")
                .address("서울시 메시")
                .build();

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    void 회원가입_테스트() {
        Member member = ex_dto();
        Member savedMember = memberService.saveMember(member);

        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
    }

    @Test
    void 중복_테스트() {
        Member member1 = ex_dto();
        Member member2 = ex_dto();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2); });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}