package make.web.service;

import make.web.constant.SellStatus;
import make.web.dto.CartItemDto;
import make.web.entity.CartItem;
import make.web.entity.Item;
import make.web.entity.Member;
import make.web.repository.CartItemRepository;
import make.web.repository.ItemRepository;
import make.web.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .region("서울")
                .sellStatus(SellStatus.SELL)
                .price(10000)
                .detail("테스트 상품 입니다.")
                .build();

        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.builder()
                .email("test@test.test")
                .build();

        return memberRepository.save(member);
    }

    @Test
    public void 장바구니_담기_테스트() {
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
    }
}