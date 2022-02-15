package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.dto.CartItemDto;
import make.web.dto.CartListDto;
import make.web.dto.ItemSearchDto;
import make.web.dto.MainItemDto;
import make.web.entity.Item;
import make.web.entity.Member;
import make.web.service.CartService;
import make.web.service.ItemService;
import make.web.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ItemService itemService;
    private final MemberService memberService;

    @PostMapping("/cart")
    public @ResponseBody
    ResponseEntity addCart(@RequestBody @Valid CartItemDto cartItemDto, Model model,
                           BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String seller = itemService.getSeller(cartItemDto.getItemId());

        if(seller.equals(email)) {
            return new ResponseEntity<String>("본인이 판매하는 상품입니다.", HttpStatus.BAD_REQUEST);
        }

        log.info("장바구니에 담기 성공");

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String cartManage(Principal principal, Model model) {

        log.info("장바구니 리스트 읽기");

        List<CartListDto> cartList = cartService.getCartList(principal.getName());

        model.addAttribute("cartItems", cartList);

        return "cart/cartList";

        /*

        String email = principal.getName();
        Long id = cartService.getCartId(email);

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<CartListDto> items = cartService.getCart(itemSearchDto, pageable, id);

        model.addAttribute("items", items);
        model.addAttribute("dto", itemSearchDto);
        model.addAttribute("maxPage", 5); //페이지 수를 5개씩 끊음

        return "cart/cartListV2";

         */
    }

    @DeleteMapping("/cart/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        if(!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

}
