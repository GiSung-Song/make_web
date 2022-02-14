package make.web.Message;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {

    private Long id;

    private String sendTo;

    private String sendFrom;

    private String content;

    private LocalDateTime sendTime;

    private LocalDateTime readTime;

    private Boolean confirm;

    @QueryProjection
    public MessageDto(Long id, String sendTo, String sendFrom, String content, LocalDateTime sendTime,
                      LocalDateTime readTime, Boolean confirm) {
        this.id = id;
        this.sendTo = sendTo;
        this.sendFrom = sendFrom;
        this.content = content;
        this.sendTime = sendTime;
        this.readTime = readTime;
        this.confirm = confirm;
    }
}
