package make.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.constant.Role;
import make.web.dto.MemberFormDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //회원 번호

    private String name; //회원 이름

    @Column(unique = true)
    private String email; //이메일

    private String password; //비밀번호

    private String address; //주소

    @Enumerated(EnumType.STRING)
    private Role role; //ADMIN or USER or GUEST

    @Builder
    public Member(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    //일반 USER 생성
    public static Member createUser(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();

        return member;
    }
}
