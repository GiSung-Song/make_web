package make.web.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberFormDto {

    private Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자로 입력해주세요.")
    private String name; //이름

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email; //이메일

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자로 입력해주세요.")
    private String password; //비밀번호

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address; //주소

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})$", message = "###-####-#### 방식으로 입력해주세요.")
    private String phone;

    @Builder
    public MemberFormDto(String name, String email, String password, String address, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }
}
