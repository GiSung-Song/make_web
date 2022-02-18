package make.web.message.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.message.MessageStatus;
import make.web.member.entity.Member;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "message")
@DynamicUpdate
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    private Long id; //메시지 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "send_to")
    private Member sendTo; //받는 이

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "send_from")
    private Member sendFrom; //보내는 이

    private String title; //메시지 제목

    private String content; //내용

    private MessageStatus confirm; //읽었는지 여부

    private LocalDateTime readTime; //읽은 시간

    @CreatedDate
    private LocalDateTime sendTime; //보낸 시간

    @Builder
    public Message(Member sendTo, Member sendFrom, String content, String title) {
        this.sendTo = sendTo;
        this.sendFrom = sendFrom;
        this.content = content;
        this.title = title;
        this.confirm = MessageStatus.NOT_READ;
        this.sendTime = LocalDateTime.now();
    }

    public void readFirst() {
        this.readTime = LocalDateTime.now();
        this.confirm = MessageStatus.READ;
    }

    public boolean isFirst() {
        return this.confirm == MessageStatus.NOT_READ ? true : false;
    }

}
