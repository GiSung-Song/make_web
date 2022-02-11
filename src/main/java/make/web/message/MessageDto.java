package make.web.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    private String sendTo;

    private String sendFrom;

    private String content;

}
