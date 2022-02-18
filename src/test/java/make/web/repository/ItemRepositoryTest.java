package make.web.repository;

import make.web.etc.constant.SellStatus;
import make.web.item.entity.Item;
import make.web.item.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void 상품_저장_테스트() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(1000)
                .detail("테스트 내용")
                .sellStatus(SellStatus.SELL)
                .build();

        itemRepository.save(item);

        Item findCoffee = itemRepository.findByItemNm("테스트 상품");

        assertThat(item.getItemNm()).isEqualTo(findCoffee.getItemNm());
    }


}