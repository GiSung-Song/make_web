package make.web.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class EditFormDto {

    private String name;

    private String email;

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})$", message = "###-####-#### 방식으로 입력해주세요.")
    private String phone;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;
}
