package make.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberFormDto {

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
}
