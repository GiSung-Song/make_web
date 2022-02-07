package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.dto.CartItemDto;
import make.web.service.CartService;
import make.web.service.ItemService;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ItemService itemService;

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

}
