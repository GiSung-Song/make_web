package make.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.constant.Role;
import make.web.dto.MemberFormDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //회원 번호

    private String name; //회원 이름

    @Column(unique = true)
    private String phone; //전화번호

    @Column(unique = true)
    private String email; //이메일

    private String password; //비밀번호

    private String address; //주소

    @Enumerated(EnumType.STRING)
    private Role role; //ADMIN or USER or GUEST

   @Builder
    public Member(String name, String email, String password, String address, Role role, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.phone = phone;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        String dtoPhone = memberFormDto.getPhone();
        dtoPhone = dtoPhone.replaceAll("-", "");

        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .phone(dtoPhone)
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();

        return member;
    }
}

