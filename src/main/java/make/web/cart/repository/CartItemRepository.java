package make.web.cart.repository;

import make.web.cart.dto.CartListDto;
import make.web.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new make.web.cart.dto.CartListDto(ci.id, i.itemNm, i.price, im.imgUrl, i.region, i.id) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.imgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartListDto> findCartListDto(@Param("cartId") Long cartId);
}
