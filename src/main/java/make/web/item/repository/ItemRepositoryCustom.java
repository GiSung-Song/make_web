package make.web.item.repository;

import make.web.item.dto.ItemSearchDto;
import make.web.item.dto.MainItemDto;
import make.web.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getSellPage(ItemSearchDto dto, Pageable pageable, Long memberId);

    Page<MainItemDto> getMainPage(ItemSearchDto dto, Pageable pageable);

//    Page<CartListDto> getCartPage(ItemSearchDto dto, Pageable pageable, Long memberId);
}
