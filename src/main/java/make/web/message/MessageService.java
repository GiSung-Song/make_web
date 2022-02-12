package make.web.message;

import lombok.RequiredArgsConstructor;
import make.web.entity.Member;
import make.web.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    public void sendMessage(String email, MessageDto dto) {

        Member sendTo = memberRepository.findByEmail(email);
        Member sendFrom = memberRepository.findByEmail(dto.getSendFrom());

        Message message = Message.builder()
                .sendTo(sendTo)
                .sendFrom(sendFrom)
                .content(dto.getContent())
                .build();

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> readMessage(String email) {

        List<MessageDto> messageList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        messageList = messageRepository.findGetMessageList(member.getId());

        return messageList;
    }

}
