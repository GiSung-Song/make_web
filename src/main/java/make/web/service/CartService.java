package make.web.service;

import lombok.RequiredArgsConstructor;
import make.web.dto.CartItemDto;
import make.web.entity.Cart;
import make.web.entity.CartItem;
import make.web.entity.Item;
import make.web.entity.Member;
import make.web.repository.CartItemRepository;
import make.web.repository.CartRepository;
import make.web.repository.ItemRepository;
import make.web.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null) {
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }


}
