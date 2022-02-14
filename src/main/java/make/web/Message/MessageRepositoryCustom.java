package make.web.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {

    Page<MessageDto> getMessageList(MessageSearchDto dto, Pageable pageable);

}
