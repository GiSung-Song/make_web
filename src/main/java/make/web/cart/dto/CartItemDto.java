package make.web.cart.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull(message = "상품 id는 필수 입력 값입니다.")
    private Long itemId;
}
