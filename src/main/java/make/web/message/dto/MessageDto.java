package make.web.message.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import make.web.message.MessageStatus;
import make.web.message.entity.Message;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class MessageDto {

    private Long id;

    private String sendTo;

    private String sendFrom;

    private String content;

    private String title;

    private LocalDateTime sendTime;

    private LocalDateTime readTime;

    private MessageStatus confirm;

    public static MessageDto of(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .sendTo(message.getSendTo().getEmail())
                .sendFrom(message.getSendFrom().getEmail())
                .content(message.getContent())
                .sendTime(message.getSendTime())
                .readTime(message.getReadTime())
                .confirm(message.getConfirm())
                .title(message.getTitle())
                .build();
    }

}
