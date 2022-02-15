package make.web.Message.repository;

import make.web.Message.dto.MessageSearchDto;
import make.web.Message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {

    Page<Message> getMessageList(MessageSearchDto dto, Pageable pageable, Long memberId);

}
