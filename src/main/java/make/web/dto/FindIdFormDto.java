package make.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FindIdFormDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자로 입력해주세요.")
    private String name; //이름

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])([0-9]{3}|[0-9]{4})([0-9]{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;


}