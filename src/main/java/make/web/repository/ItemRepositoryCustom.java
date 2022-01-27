package make.web.repository;

import make.web.dto.ItemSearchDto;
import make.web.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getItemPage(ItemSearchDto dto, Pageable pageable, Long memberId);

}
