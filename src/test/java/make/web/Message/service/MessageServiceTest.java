package make.web.Message.service;

import make.web.Message.dto.SendMessageDto;
import make.web.Message.entity.Message;
import make.web.Message.repository.MessageRepository;
import make.web.entity.Member;
import make.web.repository.MemberRepository;
import make.web.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class MessageServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MemberService memberService;

    @Test
    void 메시지_저장_테스트() {
        Member sender = Member.builder()
                .email("sender@test.test")
                .build();

        memberRepository.save(sender);

        Member receiver = Member.builder()
                .email("receiver@test.test")
                .build();

        memberRepository.save(receiver);

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setSendTo("sender@test.test");
        sendMessageDto.setSendFrom("receiver@test.test");
        sendMessageDto.setContent("테스트 내용입니다.");


        try {
            Long messageId = messageService.sendMessage(sendMessageDto);


            Message savedMessage = messageRepository.findById(messageId)
                    .orElseThrow(EntityNotFoundException::new);

            assertEquals(savedMessage.getSendFrom().getEmail(), receiver.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}