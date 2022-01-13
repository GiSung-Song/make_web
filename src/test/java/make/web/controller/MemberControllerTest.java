package make.web.controller;

import make.web.dto.MemberFormDto;
import make.web.entity.Member;
import make.web.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc; //웹 브라우저에서 요청하는 것처럼 테스트할 수 있도록 하는 가짜 객체

    Member 멤버_생성(String email, String password) {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email(email)
                .name("테스트")
                .password(password)
                .address("서울시 메시")
                .build();

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    void 로그인_성공() throws Exception {
        String email = "test@test.test";
        String password = "test1234";
        this.멤버_생성(email, password);

        mockMvc.perform(formLogin().userParameter("email")
        .loginProcessingUrl("/member/login")
        .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    void 로그인_실패() throws Exception {
        String email = "test@test.test";
        String password = "test1234";
        this.멤버_생성(email, password);

        /* mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/member/login")
                .user("test@test.tes").password(password))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
         */

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/member/login")
                .user(email).password("test123"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());


    }
}