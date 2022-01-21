package make.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class FindPwFormDto {

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})$", message = "###-####-#### 방식으로 입력해주세요.")
    private String phone;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자로 입력해주세요.")
    private String name; //이름

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email; //이메일

}
