package make.web.Message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageDto {

    private String sendFrom;

    private String sendTo;

    private String content;

}
