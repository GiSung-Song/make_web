package make.web.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import make.web.constant.SellStatus;
import make.web.dto.*;
import make.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory; //동적 쿼리 생성

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //판매중이거나 판매완료인 경우
    private BooleanExpression searchSellStatusEq(SellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.sellStatus.eq(searchSellStatus);
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

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if(StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getSellPage(ItemSearchDto dto, Pageable pageable, Long memberId) {
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(QItem.item.member.id.eq(memberId),
                        regDateAfter(dto.getSearchDateType()),
                        searchSellStatusEq(dto.getSearchSellStatus()),
                        searchByLike(dto.getSearchBy(),
                        dto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainPage(ItemSearchDto dto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg img = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(new QMainItemDto(
                        item.id,
                        item.itemNm,
                        item.detail,
                        img.imgUrl,
                        item.price,
                        item.region
                ))
                .from(img)
                .join(img.item, item)
                .where(img.imgYn.eq("Y"))
                .where(item.sellStatus.eq(SellStatus.SELL))
                .where(itemNmLike(dto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
