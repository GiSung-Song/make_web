package make.web.message.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import make.web.message.dto.MessageSearchDto;
import make.web.message.MessageStatus;

import make.web.message.entity.Message;
import make.web.message.entity.QMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class MessageRepositoryCustomImpl implements MessageRepositoryCustom{

    private JPAQueryFactory queryFactory; //동적 쿼리 생성

    public MessageRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    @Override
    public Page<Message> getMessageList(MessageSearchDto dto, Pageable pageable, Long memberId) {
        QMessage message = QMessage.message;

        QueryResults<Message> results = queryFactory
                .selectFrom(message)
                .where(message.sendTo.id.eq(memberId),
                        regDateAfter(dto.getSearchDateType()),
                        confirmCheck(dto.getSearchConfirm()),
                        titleNmLike(dto.getSearchQuery()))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Message> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Message> sendMessageList(MessageSearchDto dto, Pageable pageable, Long memberId) {
        QMessage message = QMessage.message;

        QueryResults<Message> results = queryFactory
                .selectFrom(message)
                .where(message.sendFrom.id.eq(memberId),
                        regDateAfter(dto.getSearchDateType()),
                        confirmCheck(dto.getSearchConfirm()),
                        titleNmLike(dto.getSearchQuery()))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Message> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression confirmCheck(MessageStatus confirm) {
        return confirm == null ? null : QMessage.message.confirm.eq(confirm);
    }

    private BooleanExpression titleNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QMessage.message.title.like("%" + searchQuery + "%");
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
