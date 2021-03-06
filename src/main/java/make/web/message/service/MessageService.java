package make.web.message.service;

import lombok.RequiredArgsConstructor;
import make.web.message.dto.SendMessageDto;
import make.web.message.entity.Message;
import make.web.message.dto.MessageDto;
import make.web.message.repository.MessageRepository;
import make.web.message.dto.MessageSearchDto;
import make.web.member.entity.Member;
import make.web.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<MessageDto> getMessageList(MessageSearchDto messageSearchDto, Pageable pageable, Long memberId) {
        Page<Message> entity = messageRepository.getMessageList(messageSearchDto, pageable, memberId);

        return entity.map(MessageDto::of);
    }

    @Transactional(readOnly = true)
    public Page<MessageDto> sendMessageList(MessageSearchDto messageSearchDto, Pageable pageable, Long memberId) {
        Page<Message> entity = messageRepository.sendMessageList(messageSearchDto, pageable, memberId);

        return entity.map(MessageDto::of);
    }

    public Long sendMessage(SendMessageDto dto) throws Exception {
        Member sender = memberRepository.findByEmail(dto.getSendFrom());
        Member receiver = memberRepository.findByEmail(dto.getSendTo());

        Message message = Message.builder()
                .sendFrom(sender)
                .sendTo(receiver)
                .content(dto.getContent())
                .title(dto.getTitle())
                .build();

        messageRepository.save(message);

        return message.getId();
    }

    public MessageDto readMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(EntityNotFoundException::new);

        if(message.isFirst())
            message.readFirst();

        MessageDto messageDto = MessageDto.of(message);
        return messageDto;
    }

}
