package make.web.message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findBySendFrom(String sendFrom);

    Message findBySendTo(String sendTo);

}
