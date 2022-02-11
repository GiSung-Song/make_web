package make.web.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findBySendFrom(String sendFrom);

    Message findBySendTo(String sendTo);

    @Query("select new make.web.message.MessageDto(m.sendTo, m.sendFrom, m.content, m.sendTime) " +
            "from Message m " +
            "where m.sendTo.email = :email " +
            "order by m.sendTime desc"
    )
    List<MessageDto> findGetMessageList(@Param("email") String email);

}
