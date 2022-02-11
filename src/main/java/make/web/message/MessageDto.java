package make.web.message;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {

    private String sendTo;

    private String sendFrom;

    private String content;

    private LocalDateTime sendTime;

    @QueryProjection
    public MessageDto(String sendTo, String sendFrom, String content, LocalDateTime sendTime) {
        this.sendTo = sendTo;
        this.sendFrom = sendFrom;
        this.content = content;
        this.sendTime = sendTime;
    }
}
