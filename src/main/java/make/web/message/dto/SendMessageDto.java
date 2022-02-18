package make.web.message.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SendMessageDto {

    @NotEmpty
    private String sendFrom;

    @NotEmpty
    private String title;

    @NotEmpty(message = "제목을 입력해주세요.")
    private String sendTo;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

}
