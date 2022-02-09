package make.web.repository;

import make.web.dto.CartListDto;
import make.web.dto.ItemSearchDto;
import make.web.dto.MainItemDto;
import make.web.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getSellPage(ItemSearchDto dto, Pageable pageable, Long memberId);

    Page<MainItemDto> getMainPage(ItemSearchDto dto, Pageable pageable);
}
