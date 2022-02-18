package make.web.message.repository;

import make.web.message.dto.MessageSearchDto;
import make.web.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {

    Page<Message> getMessageList(MessageSearchDto dto, Pageable pageable, Long memberId);

}
