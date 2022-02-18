package make.web.cart.service;

import lombok.RequiredArgsConstructor;
import make.web.cart.dto.CartItemDto;
import make.web.cart.dto.CartListDto;
import make.web.cart.entity.Cart;
import make.web.cart.entity.CartItem;
import make.web.item.entity.Item;
import make.web.member.entity.Member;
import make.web.cart.repository.CartItemRepository;
import make.web.cart.repository.CartRepository;
import make.web.item.repository.ItemRepository;
import make.web.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<CartListDto> getCartList(String email) {

        List<CartListDto> dtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null) {
            return dtoList;
        }

        dtoList = cartItemRepository.findCartListDto(cart.getId());

        return dtoList;
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member member = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    /*

    public Long getCartId(String email) {
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        return cart.getId();
    }

    @Transactional(readOnly = true)
    public Page<CartListDto> getCart(ItemSearchDto itemSearchDto, Pageable pageable, Long cartId) {
        return itemRepository.getCartPage(itemSearchDto, pageable, cartId);
    }
     */

}
