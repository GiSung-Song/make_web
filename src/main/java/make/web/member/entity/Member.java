package make.web.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.etc.constant.Role;
import make.web.member.dto.EditFormDto;
import make.web.member.dto.MemberFormDto;
import make.web.etc.entity.BaseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

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

    @Transient
    public String key;

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

        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .phone(memberFormDto.getPhone())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();

        return member;
    }

    public void editMember(EditFormDto dto) {

       String phone = dto.getPhone();
//       phone = phone.replaceAll("-", "");

       this.address = dto.getAddress();
       this.phone = phone;
    }

    public void addKey(String key) {
       this.key = key;
    }

    //임시 비밀번호로 설정
    public void editPass(String password, PasswordEncoder passwordEncoder) {
       this.password = passwordEncoder.encode(password);
    }
}

