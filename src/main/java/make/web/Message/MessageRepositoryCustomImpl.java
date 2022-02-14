package make.web.Message;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import make.web.entity.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class MessageRepositoryCustomImpl implements MessageRepositoryCustom{

    private JPAQueryFactory queryFactory; //동적 쿼리 생성

    public MessageRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    @Override
    public Page<MessageDto> getMessageList(MessageSearchDto dto, Pageable pageable) {
        QMessage message = QMessage.message;
        QMember member = QMember.member;

        QueryResults<MessageDto> results = queryFactory
                .select(new QMessageDto(
                        message.id,
                        message.sendTo,
                        message.sendFrom,
                        message.content,
                        message.sendTime,
                        message.readTime,
                        message.confirm
                ))
                .from(message)
                .join(message)
                .where(message.sendTo.id.eq(message.id))
    }

    private BooleanExpression confirmCheck(boolean confirm) {
        return QMessage.message.confirm.eq(confirm);
    }

    private BooleanExpression regDateAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); //현재 시각과 비교하기 위해

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QMessage.message.sendTime.after(dateTime);
    }


}
